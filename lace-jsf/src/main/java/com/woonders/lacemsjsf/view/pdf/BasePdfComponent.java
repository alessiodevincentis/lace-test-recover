package com.woonders.lacemsjsf.view.pdf;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.woonders.lacemscommon.app.viewmodel.FinanziariaViewModel;
import com.woonders.lacemscommon.config.AWSConfiguration;
import com.woonders.lacemscommon.config.Constants;
import com.woonders.lacemsjsf.db.app.config.RequestTenantIdentifierResolver;
import com.woonders.lacemsjsf.exception.NoFileOnS3Exception;
import enumPdf.PdfFileType;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by emanuele on 26/11/16.
 */
@Slf4j
public abstract class BasePdfComponent {

	private static final String BASE_PDF_PATH = "config/pdf/";
	@Autowired
	private RequestTenantIdentifierResolver requestTenantIdentifierResolver;
	@Autowired
	private AmazonS3 amazonS3;

	public PDDocument loadPDDocumentFromDisk(final PdfFileType pdfFileType, final FinanziariaViewModel finanziariaViewModel) throws IOException, NoFileOnS3Exception {
		byte[] pdfContentToLoad = null;
			String filePath = addTenantPath(BASE_PDF_PATH).concat(addCurrentTenant());
			switch (pdfFileType) {
				case PREFIN:
				case ALLEGATI:
				case SCHEDA_ATC:
					filePath = addFinanziariaPath(filePath);
					pdfContentToLoad = loadPdf(filePath.concat(addCurrentFinanziaria(finanziariaViewModel)).concat(pdfFileType.getName()));
					break;
				case ANAGRAFICA:
				case PRIVACY:
					pdfContentToLoad = loadPdf(filePath.concat(pdfFileType.getName()));
					break;
				case MARKETING:
					pdfContentToLoad = loadPdf(BASE_PDF_PATH.concat(pdfFileType.getName()));
					break;
			}
		if (pdfContentToLoad != null) {
			return PDDocument.load(pdfContentToLoad);
		} else {
				byte[] defaultContent = loadPdf(BASE_PDF_PATH.concat(PdfFileType.DEFAULT_TEMPLATE.getName()));
				if(defaultContent == null) {
					throw new IOException("Default content can't be read from S3, please check NOW!");
				} else {
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(defaultContent.length);
					byteArrayOutputStream.write(defaultContent);
					throw new NoFileOnS3Exception(byteArrayOutputStream);
				}
		}
	}

	private byte[] loadPdf(final String fileToRetrieve) {
		if (!checkIfLocalFileExists(fileToRetrieve)) {
			final byte[] content = getPdfFileFromS3(fileToRetrieve);
			try {
				writeFileToDisk(fileToRetrieve, content);
			} catch (IOException e) {
				log.error("Unable to write file", e);
			}
			return content;
		} else {
			try {
				return readFileFromDisk(fileToRetrieve);
			} catch (IOException e) {
				log.error("Unable to read file", e);
			}
		}
		return null;
	}

	private boolean checkIfLocalFileExists(final String filePath) {
		return Files.exists(Paths.get(Constants.APP_WORKING_DIR + filePath));
	}

	private byte[] readFileFromDisk(final String filePath) throws IOException {
		return Files.readAllBytes(Paths.get(Constants.APP_WORKING_DIR + filePath));
	}

	private void writeFileToDisk(final String filePath, byte[] content) throws IOException {
		if(content == null) {
			throw new IOException();
		}
		Path fileToWritePath = Paths.get(Constants.APP_WORKING_DIR + filePath);
		if(fileToWritePath.getParent() != null && !fileToWritePath.getParent().toFile().exists()) {
			boolean dirsCreated = fileToWritePath.getParent().toFile().mkdirs();
			if(!dirsCreated) {
				log.error("Unable to create dir " + fileToWritePath);
				throw new IOException();
			}
		}
		Files.write(fileToWritePath, content);
	}

	private byte[] getPdfFileFromS3(final String fileKey) {
		final GetObjectRequest getObjectRequest = new GetObjectRequest(AWSConfiguration.BUCKET_NAME, fileKey);
		S3ObjectInputStream s3ObjectInputStream = null;
		try {
			final S3Object s3Object = amazonS3.getObject(getObjectRequest);
			s3ObjectInputStream = s3Object.getObjectContent();
			return IOUtils.toByteArray(s3ObjectInputStream);
		} catch (final AmazonClientException | IOException e) {
			return null;
		} finally {
			IOUtils.closeQuietly(s3ObjectInputStream);
		}
	}

	private String addTenantPath(final String currentPath) {
		final String tenantPath = "agenzia/";
		return currentPath + tenantPath;
	}

	private String addFinanziariaPath(final String currentPath) {
		final String finanziariaPath = "finanziaria/";
		return currentPath + finanziariaPath;
	}

	private String addCurrentTenant() {
		return requestTenantIdentifierResolver.getTenantIdentifier().toLowerCase() + "/";
	}

	private String addCurrentFinanziaria(FinanziariaViewModel finanziariaViewModel) {
		return finanziariaViewModel.getName().toLowerCase() + "/";
	}

}
