package com.woonders.lacemscommon.app.campagneexcel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.woonders.lacemscommon.app.viewmodel.ClienteViewModel;
import com.woonders.lacemscommon.exception.CannotImportExcelException;
import com.woonders.lacemscommon.exception.ExcelImportRowsExceeded;
import com.woonders.lacemscommon.exception.FileExcelToImportNotValidException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ApachePOIExcelRead {

	public static final String NAME = "apachePOIExcelRead";
	public static final String JSF_NAME = "#{" + NAME + "}";
	public static final int MAX_EXCEL_ROWS_IMPORT = 10000;
	private static final String REGEX_LETTERS = "^[ 'A-Za-z]+$";
	private static final String REGEX_MOBILE = "^\\+?\\d+$";
	private static final String CONTENT_TYPE_XLS = "application/vnd.ms-excel";

	public List<ClienteViewModel> getRowExcel(InputStream excelFile, String contentType)
			throws IOException, CannotImportExcelException, NoSuchElementException, ExcelImportRowsExceeded,
			FileExcelToImportNotValidException {

		Workbook workbook = null;
		try {
			if (contentType.equalsIgnoreCase(CONTENT_TYPE_XLS)) {
				workbook = new HSSFWorkbook(excelFile);
			} else {
				workbook = new XSSFWorkbook(excelFile);
			}
		} catch (Exception e) {
			throw new FileExcelToImportNotValidException();
		}

		Sheet datatypeSheet = workbook.getSheetAt(0);
		List<ClienteViewModel> leadFromExcel = new ArrayList<>();

		if (datatypeSheet.getPhysicalNumberOfRows() > MAX_EXCEL_ROWS_IMPORT) {
			throw new ExcelImportRowsExceeded();
		}

		for (int i = 0; i < datatypeSheet.getPhysicalNumberOfRows(); i++) {
			ClienteViewModel clienteViewModel = new ClienteViewModel();
			Cell nomeCell = datatypeSheet.getRow(i).getCell(0);
			if (nomeCell != null) {
				nomeCell.setCellType(CellType.STRING);
			}
			Cell cognomeCell = datatypeSheet.getRow(i).getCell(1);
			if (cognomeCell != null) {
				cognomeCell.setCellType(CellType.STRING);
			}
			Cell telefonoCell = datatypeSheet.getRow(i).getCell(2);
			if (telefonoCell != null) {
				telefonoCell.setCellType(CellType.STRING);
			}

			int numberRowError = datatypeSheet.getRow(i).getRowNum() + 1;

			if (nomeCell == null || cognomeCell == null || telefonoCell == null) {
				log.info("Importazione Fallita. Errore nella riga: " + numberRowError);
				throw new CannotImportExcelException(Integer.toString(numberRowError));
			}

			String nome = nomeCell.getStringCellValue();
			String cognome = cognomeCell.getStringCellValue();
			String telefono = StringUtils.deleteWhitespace(telefonoCell.getStringCellValue());

			if (StringUtils.isEmpty(nome) || !nome.matches(REGEX_LETTERS) || StringUtils.isEmpty(cognome)
					|| !cognome.matches(REGEX_LETTERS) || StringUtils.isEmpty(telefono)
					|| !telefono.matches(REGEX_MOBILE)) {

				log.info("Importazione Fallita. Errore nella riga: " + numberRowError);
				throw new CannotImportExcelException(Integer.toString(numberRowError));
			}

			clienteViewModel.setNome(nome);
			clienteViewModel.setCognome(cognome);
			clienteViewModel.setTelefono(telefono);
			leadFromExcel.add(clienteViewModel);
		}
		return leadFromExcel;
	}
}
