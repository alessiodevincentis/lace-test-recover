package com.woonders.lacemscommon.app.backupexcel;

import com.woonders.lacemscommon.app.viewmodel.*;
import com.woonders.lacemscommon.config.async.AsyncConfig;
import com.woonders.lacemscommon.exception.UnableToSaveFileException;
import com.woonders.lacemscommon.exception.UnableToSendEmailException;
import com.woonders.lacemscommon.service.BackupService;
import com.woonders.lacemscommon.util.DateToCalendar;
import com.woonders.lacemscommon.util.LaceMailSender;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.List;

@Component
public class ExcelWriterComponent {

    public static final String NAME = "excelWriterComponent";
    public static final String JSF_NAME = "#{" + NAME + "}";
    private static final String CF = "CF";
    private static final String COGNOME = "Cognome";
    private static final String NOME = "Nome";
    private static final String VIEWID = "viewId";
    private static final String CODICEPRATICA = "Codice Pratica";
    private static final String OPERATORE = "operatore";
    private static final int PAGE_SIZE = 50;
    private static final int COUNT_PASSWORD_SIZE = 5;
    @Autowired
    private BackupService backupService;
    @Autowired
    private LaceMailSender laceMailSender;

    //https://stackoverflow.com/questions/2498536/poi-performance
    private Workbook createWorkbook() {
        final SXSSFWorkbook workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(true);
        return workbook;
    }

    private Sheet createSheet(final Workbook workbook, final String sheetName) {
        final SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet(sheetName);
        sheet.setRandomAccessWindowSize(PAGE_SIZE);
        return sheet;
    }

    @Async(AsyncConfig.SINGLE_TASK_EXECUTOR)
    public void startBackupFile(final String tenantId, final String filename, final String to, final String agencyName,
                                final String replyTo, final String subject, final String body)
            throws IllegalArgumentException, IllegalAccessException, IOException, UnableToSaveFileException, UnableToSendEmailException {
        Workbook workbook = createWorkbook();
        workbook = writeClienti(workbook);
        workbook = writeNominativi(workbook);
        workbook = writeAmministrazioni(workbook);
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            backupService.saveBackupFile(tenantId, filename, byteArrayOutputStream.toByteArray());
            final String password = RandomStringUtils.randomAlphanumeric(COUNT_PASSWORD_SIZE);
            backupService.updatePasswordBackup(password);
            laceMailSender.sendEmail(to, agencyName, replyTo, subject, MessageFormat.format(body, password, null));
        }
    }

    private Workbook writeNominativi(final Workbook workbook) throws IllegalAccessException {
        // SEZIONE NOMINATIVI //

        int firstElement = 1;
        int firstElementImpegni = 1;
        List<ClienteViewModel> nominativiViewModelList;

        // Nominativi
        // create new page of xls
        final Sheet sheetNominativi = createSheet(workbook, "Nominativi");
        writeFirstRowNominativi(sheetNominativi);

        // Nominativi - Impegni
        final Sheet sheetNominativiImpegni = createSheet(workbook, "Impegni - Nominativi");
        writeFirstRowNominativiImpegni(sheetNominativiImpegni);

        do {
            nominativiViewModelList = backupService.findAllNominativi(firstElement, PAGE_SIZE);
            writeNominativi(firstElement, sheetNominativi, nominativiViewModelList);
            final int impegniWritten = writeOnCellNominativiImpegni(firstElementImpegni, sheetNominativiImpegni, nominativiViewModelList);

            firstElement = firstElement + PAGE_SIZE;
            firstElementImpegni = firstElementImpegni + impegniWritten;

        }
        while (nominativiViewModelList.size() == PAGE_SIZE);
        return workbook;
    }

    private Workbook writeClienti(final Workbook workbook) throws IllegalAccessException {
        // SEZIONE CLIENTI //

        int firstElement = 1;
        int firstElementPratiche = 1;
        int firstElementTrattenute = 1;
        int firstElementEstinzioni = 1;
        List<ClienteViewModel> clienteViewModelList;

        // Clienti
        final Sheet sheetClienti = createSheet(workbook, "Clienti");
        writeFirstRowClienti(sheetClienti);

        // Pratiche
        final Sheet sheetPratiche = createSheet(workbook, "Pratiche");
        writeFirstRowPraticheCliente(sheetPratiche);

        // Trattenute Clienti
        final Sheet sheetTrattenuteClienti = createSheet(workbook, "Trattenute - Clienti");
        writeFirstRowTrattenuteCliente(sheetTrattenuteClienti);

        // Estinzioni Pratiche
        final Sheet sheetEstinzioniPratiche = createSheet(workbook, "Estinzioni - Pratiche");
        writeFirstRowEstinzionePratiche(sheetEstinzioniPratiche);

        do {
            clienteViewModelList = backupService.findAllPratiche(firstElement, PAGE_SIZE);
            writeOnCellClienti(firstElement, clienteViewModelList, sheetClienti);
            final int praticheWritten = writeOnCellPraticheCliente(firstElementPratiche, sheetPratiche, clienteViewModelList);
            final int trattenuteWritten = writeOnCellTrattenuteCliente(firstElementTrattenute, sheetTrattenuteClienti, clienteViewModelList);
            final int estinzioniWritten = writeOnCellEstinzionePratiche(firstElementEstinzioni, sheetEstinzioniPratiche, clienteViewModelList);

            firstElement = firstElement + PAGE_SIZE;
            firstElementPratiche = firstElementPratiche + praticheWritten;
            firstElementTrattenute = firstElementTrattenute + trattenuteWritten;
            firstElementEstinzioni = firstElementEstinzioni + estinzioniWritten;
        }
        while (clienteViewModelList.size() == PAGE_SIZE);
        return workbook;
    }

    private Workbook writeAmministrazioni(final Workbook workbook) throws IllegalAccessException {
        // SEZIONE AMMINISTRAZIONI //

        int firstElement = 1;
        int firstElementValutazioni = 1;
        List<AmministrazioneViewModel> amministrazioneViewModelList;

        // Amministrazioni
        final Sheet sheetAmministrazioni = createSheet(workbook, "Amministrazioni");
        writeFirstRowAmministrazioni(sheetAmministrazioni);

        // Amministrazioni valutazioni
        final Sheet sheetValutazioniAmministrazioni = createSheet(workbook, "Valutazioni - Amministrazioni");
        writeFirstRowValutazioneAmministrazioni(sheetValutazioniAmministrazioni);

        do {
            amministrazioneViewModelList = backupService.findAllAmministrazioni(firstElement, PAGE_SIZE);
            writeOnCellAmministrazioni(firstElement, sheetAmministrazioni, amministrazioneViewModelList);
            final int valutazioniWritten = writeOnCellValutazioneAmministrazioni(firstElementValutazioni, sheetValutazioniAmministrazioni, amministrazioneViewModelList);

            firstElement = firstElement + PAGE_SIZE;
            firstElementValutazioni = firstElementValutazioni + valutazioniWritten;
        }
        while (amministrazioneViewModelList.size() == PAGE_SIZE);
        return workbook;
    }

    private Field[] getClienteFields() {
        return ClienteViewModel.class.getDeclaredFields();
    }

    private Field[] getResidenzaFields() {
        return ResidenzaViewModel.class.getDeclaredFields();
    }

    private Field[] getDocumentoFields() {
        return DocumentoViewModel.class.getDeclaredFields();
    }

    private Field[] getContoFields() {
        return ContoViewModel.class.getDeclaredFields();
    }

    private Field[] getTrattenuteFields() {
        return TrattenuteViewModel.class.getDeclaredFields();
    }

    private Field[] getAmministrazioneFields() {
        return AmministrazioneViewModel.class.getDeclaredFields();
    }

    private Field[] getValutazioneAmministrazioneFields() {
        return ValutazioneAmministrazioneViewModel.class.getDeclaredFields();
    }

    private Field[] getPraticaFields() {
        return PraticaViewModel.class.getDeclaredFields();
    }

    private Field[] getEstinzioneFields() {
        return EstinzioneViewModel.class.getDeclaredFields();
    }

    /**
     * Foglio Nominativi
     */

    private void writeFirstRowNominativi(final Sheet sheet)
            throws IllegalArgumentException, IllegalAccessException {
        final Row row = sheet.createRow(0);
        // initialize first row with name of fields
        final int colNum = 0;
        writeOnCellNominativi(row, colNum, null, true);
    }

    private void writeNominativi(final int firstElement, final Sheet sheet, final List<ClienteViewModel> nominativi) throws IllegalArgumentException, IllegalAccessException {
        int rowNum = firstElement;
        for (final ClienteViewModel nominativo : nominativi) {
            final Row row = sheet.createRow(rowNum++);
            final int colNum = 0;
            writeOnCellNominativi(row, colNum, nominativo, false);
        }
    }

    private void writeOnCellNominativi(final Row row, int colNum,
                                       final ClienteViewModel nominativo, final boolean isFirstRow) throws IllegalArgumentException, IllegalAccessException {
        final Field[] clienteFields = getClienteFields();
        final Field[] residenzaFields = getResidenzaFields();
        for (final Field clienteField : clienteFields) {
            clienteField.setAccessible(true);
            if (clienteField.getType().equals(ResidenzaViewModel.class)) {
                for (final Field residenzaField : residenzaFields) {
                    residenzaField.setAccessible(true);
                    final Cell cell = row.createCell(colNum++);
                    if (nominativo != null) {
                        chooseValueToWriteOnCellNominativi(nominativo.getResidenzaViewModel(), residenzaField, cell,
                                isFirstRow);
                    } else {
                        chooseValueToWriteOnCellNominativi(nominativo, residenzaField, cell, isFirstRow);
                    }
                }
            } else if (clienteField.getType().equals(OperatorViewModel.class)) {
                final Cell cell = row.createCell(colNum++);
                if (nominativo != null) {
                    chooseValueToWriteOnCellNominativi(nominativo.getOperatoreNominativo(), clienteField, cell,
                            isFirstRow);
                } else {
                    chooseValueToWriteOnCellNominativi(nominativo, clienteField, cell, isFirstRow);
                }
            } else if (!clienteField.getType().equals(ContoViewModel.class)
                    && !clienteField.getType().equals(DocumentoViewModel.class)
                    && !clienteField.getType().equals(DateToCalendar.class)
                    && !clienteField.getType().equals(PreventivoViewModel.class)
                    && !clienteField.getType().equals(AmministrazioneViewModel.class)
                    && !clienteField.getType().equals(java.util.List.class)) {
                final Cell cell = row.createCell(colNum++);
                chooseValueToWriteOnCellNominativi(nominativo, clienteField, cell, isFirstRow);
            }
        }
    }

    private void chooseValueToWriteOnCellNominativi(final Object nominativo, final Field field, final Cell cell, final boolean isFirstRow)
            throws IllegalArgumentException, IllegalAccessException {
        if (!isFirstRow) {
            if (nominativo != null) {
                if (nominativo.getClass().equals(OperatorViewModel.class)) {
                    cell.setCellValue(((OperatorViewModel) nominativo).getUsername());
                } else if (field.get(nominativo) != null) {
                    cell.setCellValue(field.get(nominativo).toString());
                }
            }
        } else {
            cell.setCellValue(field.getName());
        }
    }

    /**
     * Foglio Impegni - Nominativi
     */

    private int writeOnCellNominativiImpegni(final int firstElement, final Sheet sheet, final List<ClienteViewModel> nominativi)
            throws IllegalArgumentException, IllegalAccessException {
        final Field[] trattenutaFields = getTrattenuteFields();
        int impegniWritten = 0;
        int rowNum = firstElement;
        for (final ClienteViewModel nominativo : nominativi) {
            if (nominativo != null && nominativo.getTrattenutaViewModelList() != null) {
                for (final TrattenuteViewModel trattenuta : nominativo.getTrattenutaViewModelList()) {
                    final Row row = sheet.createRow(rowNum++);
                    int colNum = 0;
                    final Cell cellCf = row.createCell(colNum++);
                    cellCf.setCellValue(nominativo.getCf());
                    final Cell cellCognome = row.createCell(colNum++);
                    cellCognome.setCellValue(nominativo.getCognome());
                    final Cell cellNome = row.createCell(colNum++);
                    cellNome.setCellValue(nominativo.getNome());
                    for (final Field trattenutaField : trattenutaFields) {
                        trattenutaField.setAccessible(true);
                        if (!trattenutaField.getName().equalsIgnoreCase(VIEWID)) {
                            final Cell cell = row.createCell(colNum++);
                            if (trattenutaField.get(trattenuta) != null) {
                                cell.setCellValue(trattenutaField.get(trattenuta).toString());
                            }
                        }
                    }
                    impegniWritten++;
                }
            }
        }
        return impegniWritten;
    }

    private void writeFirstRowNominativiImpegni(final Sheet sheet)
            throws IllegalArgumentException, IllegalAccessException {
        final Field[] trattenutaFields = getTrattenuteFields();
        final Row row = sheet.createRow(0);
        // initialize first row with name of fields
        int colNum = 0;
        final Cell cellCf = row.createCell(colNum++);
        cellCf.setCellValue(CF);
        final Cell cellCognome = row.createCell(colNum++);
        cellCognome.setCellValue(COGNOME);
        final Cell cellNome = row.createCell(colNum++);
        cellNome.setCellValue(NOME);
        for (final Field trattenutaField : trattenutaFields) {
            trattenutaField.setAccessible(true);
            if (!trattenutaField.getName().equalsIgnoreCase(VIEWID)) {
                final Cell cell = row.createCell(colNum++);
                cell.setCellValue(trattenutaField.getName());
            }
        }
    }

    /**
     * Foglio Amministrazioni
     */

    private void writeOnCellAmministrazioni(final int firstElement, final Sheet sheet,
                                            final List<AmministrazioneViewModel> amministrazioni) throws IllegalArgumentException, IllegalAccessException {
        int rowNum = firstElement;
        for (final AmministrazioneViewModel amministrazione : amministrazioni) {
            final Row row = sheet.createRow(rowNum++);
            final int colNum = 0;
            chooseValueToWriteOnCellAmministrazioni(amministrazione, false, colNum, row);
        }
    }

    private void writeFirstRowAmministrazioni(final Sheet sheet) throws IllegalArgumentException, IllegalAccessException {
        final Row row = sheet.createRow(0);
        final int colNum = 0;
        chooseValueToWriteOnCellAmministrazioni(null, true, colNum, row);
    }

    private void chooseValueToWriteOnCellAmministrazioni(final AmministrazioneViewModel amministrazione, final boolean isFirstRow, int colNum, final Row row)
            throws IllegalArgumentException, IllegalAccessException {
        final Field[] amministrazioniFields = getAmministrazioneFields();
        for (final Field amministrazioneField : amministrazioniFields) {
            amministrazioneField.setAccessible(true);
            final Cell cell = row.createCell(colNum++);
            if (!amministrazioneField.getType().equals(java.util.List.class)) {
                if (!isFirstRow) {
                    if (amministrazioneField.get(amministrazione) != null) {
                        cell.setCellValue(amministrazioneField.get(amministrazione).toString());
                    }
                } else {
                    cell.setCellValue(amministrazioneField.getName());
                }
            }
        }
    }

    /**
     * Foglio Valutazioni - Amministrazioni
     */

    private int writeOnCellValutazioneAmministrazioni(final int firstElement, final Sheet sheet, final List<AmministrazioneViewModel> amministrazioni) throws IllegalArgumentException, IllegalAccessException {
        int valutazioniWritten = 0;
        int rowNum = firstElement;
        for (final AmministrazioneViewModel amministrazione : amministrazioni) {
            if (amministrazione != null && amministrazione.getValutazioneAmministrazioneViewModelList() != null) {
                for (final ValutazioneAmministrazioneViewModel valutazione : amministrazione
                        .getValutazioneAmministrazioneViewModelList()) {
                    final Row row = sheet.createRow(rowNum++);
                    int colNum = 0;
                    final Cell cellRagioneSociale = row.createCell(colNum++);
                    cellRagioneSociale.setCellValue(amministrazione.getRagioneSociale());
                    chooseValueToWriteOnCellValutazioniAmministrazioni(valutazione, false, colNum,
                            row);
                    valutazioniWritten++;
                }
            }
        }
        return valutazioniWritten;
    }

    private void writeFirstRowValutazioneAmministrazioni(final Sheet sheet) throws IllegalArgumentException, IllegalAccessException {
        final Row row = sheet.createRow(0);
        int colNum = 0;
        final Cell cellRagioneSociale = row.createCell(colNum++);
        cellRagioneSociale.setCellValue("Ragione Sociale");
        chooseValueToWriteOnCellValutazioniAmministrazioni(null, true, colNum, row);
    }

    private void chooseValueToWriteOnCellValutazioniAmministrazioni(final ValutazioneAmministrazioneViewModel valutazione,
                                                                    final boolean isFirstRow, int colNum, final Row row)
            throws IllegalArgumentException, IllegalAccessException {
        final Field[] valutazioneFields = getValutazioneAmministrazioneFields();
        for (final Field valutazioneField : valutazioneFields) {
            valutazioneField.setAccessible(true);
            final Cell cell = row.createCell(colNum++);
            if (!isFirstRow) {
                if (valutazioneField.get(valutazione) != null) {
                    cell.setCellValue(valutazioneField.get(valutazione).toString());
                }
            } else {
                cell.setCellValue(valutazioneField.getName());
            }
        }
    }

    /**
     * Foglio Clienti
     */

    private void writeOnCellClienti(final int firstElement, final List<ClienteViewModel> clienti, final Sheet sheet)
            throws IllegalArgumentException, IllegalAccessException {
        int rowNum = firstElement;
        for (final ClienteViewModel cliente : clienti) {
            final Row row = sheet.createRow(rowNum++);
            final int colNum = 0;
            writeOnCellClienteByType(row, colNum, cliente,
                    cliente.getResidenzaViewModel(), cliente.getDocumentoViewModel(), cliente.getContoViewModel(),
                    false);
        }
    }

    private void writeFirstRowClienti(final Sheet sheet) throws IllegalArgumentException, IllegalAccessException {
        final Row row = sheet.createRow(0);
        final int colNum = 0;
        writeOnCellClienteByType(row, colNum, null, null,
                null, null, true);
    }

    private void writeOnCellClienteByType(final Row row, int colNum, final ClienteViewModel cliente, final ResidenzaViewModel residenza,
                                          final DocumentoViewModel documento, final ContoViewModel conto, final boolean isFirstRow)
            throws IllegalArgumentException, IllegalAccessException {
        final Field[] clienteFields = getClienteFields();
        final Field[] residenzaFields = getResidenzaFields();
        final Field[] documentoFields = getDocumentoFields();
        final Field[] contoFields = getContoFields();
        for (final Field clienteField : clienteFields) {
            clienteField.setAccessible(true);
            if (clienteField.getType().equals(ResidenzaViewModel.class)) {
                for (final Field field : residenzaFields) {
                    field.setAccessible(true);
                    final Cell cell = row.createCell(colNum++);
                    chooseValueToWriteOnCell(field, residenza, row, colNum, cell, isFirstRow);
                }
            } else if (clienteField.getType().equals(DocumentoViewModel.class)) {
                for (final Field field : documentoFields) {
                    field.setAccessible(true);
                    final Cell cell = row.createCell(colNum++);
                    chooseValueToWriteOnCell(field, documento, row, colNum, cell, isFirstRow);
                }
            } else if (clienteField.getType().equals(ContoViewModel.class)) {
                for (final Field field : contoFields) {
                    field.setAccessible(true);
                    final Cell cell = row.createCell(colNum++);
                    chooseValueToWriteOnCell(field, conto, row, colNum, cell, isFirstRow);
                }
            } else if (!clienteField.getType().equals(OperatorViewModel.class)
                    && !clienteField.getType().equals(DateToCalendar.class)
                    && !clienteField.getType().equals(java.util.List.class)
                    && !clienteField.getName().contains("Nominativo")) {
                final Cell cell = row.createCell(colNum++);
                chooseValueToWriteOnCell(clienteField, cliente, row, colNum, cell, isFirstRow);
            }
        }
    }

    /**
     * Foglio Pratiche
     */

    private int writeOnCellPraticheCliente(final int firstElement, final Sheet sheet, final List<ClienteViewModel> clienti)
            throws IllegalArgumentException, IllegalAccessException {
        int praticheWritten = 0;
        int rowNum = firstElement;
        for (final ClienteViewModel cliente : clienti) {
            if (cliente != null && cliente.getPraticaViewModelList() != null) {
                for (final PraticaViewModel pratica : cliente.getPraticaViewModelList()) {
                    final Row row = sheet.createRow(rowNum++);
                    final int colNum = 0;
                    String operatore = "";
                    if (pratica != null && pratica.getOperatore() != null) {
                        operatore = pratica.getOperatore().getUsername();
                    }
                    writePraticheCliente(sheet, pratica, row, colNum, cliente.getCf(),
                            cliente.getCognome(), cliente.getNome(), operatore, false);
                    praticheWritten++;
                }
            }
        }
        return praticheWritten;
    }

    private void writeFirstRowPraticheCliente(final Sheet sheet)
            throws IllegalArgumentException, IllegalAccessException {
        final Row row = sheet.createRow(0);
        // initialize first row with name of fields
        final int colNum = 0;
        writePraticheCliente(sheet, null, row, colNum, CF, COGNOME, NOME, OPERATORE, true);
    }

    private void writePraticheCliente(final Sheet sheet, final PraticaViewModel pratica, final Row row, int colNum,
                                      final String cf, final String cognome, final String nome, final String operatore, final boolean isFirstRow)
            throws IllegalArgumentException, IllegalAccessException {
        final Field[] praticaFields = getPraticaFields();
        final Cell cellCf = row.createCell(colNum++);
        cellCf.setCellValue(cf);
        final Cell cellCognome = row.createCell(colNum++);
        cellCognome.setCellValue(cognome);
        final Cell cellNome = row.createCell(colNum++);
        cellNome.setCellValue(nome);
        final Cell cellOperatore = row.createCell(colNum++);
        cellOperatore.setCellValue(operatore);
        for (final Field praticaField : praticaFields) {
            praticaField.setAccessible(true);
            if (!praticaField.getType().equals(java.util.List.class)
                    && !praticaField.getType().equals(OperatorViewModel.class)
                    && !praticaField.getType().equals(FinanziariaViewModel.class)
                    && !praticaField.getType().equals(ClienteViewModel.class)
                    && !praticaField.getName().equalsIgnoreCase(VIEWID)) {
                final Cell cell = row.createCell(colNum++);
                chooseValueToWriteOnCell(praticaField, pratica, row, colNum, cell, isFirstRow);
            }
        }
    }

    /**
     * Foglio Trattenute cliente
     */

    private void writeTrattenuteCliente(final Sheet sheet, final TrattenuteViewModel trattenuta, final Row row,
                                        int colNum, final String cf, final String cognome, final String nome, final boolean isFirstRow)
            throws IllegalArgumentException, IllegalAccessException {
        final Field[] trattenuteFields = getTrattenuteFields();
        final Cell cellCf = row.createCell(colNum++);
        cellCf.setCellValue(cf);
        final Cell cellCognome = row.createCell(colNum++);
        cellCognome.setCellValue(cognome);
        final Cell cellNome = row.createCell(colNum++);
        cellNome.setCellValue(nome);
        for (final Field trattenutaField : trattenuteFields) {
            trattenutaField.setAccessible(true);
            if (!trattenutaField.getName().equalsIgnoreCase(VIEWID)) {
                final Cell cell = row.createCell(colNum++);
                chooseValueToWriteOnCell(trattenutaField, trattenuta, row, colNum, cell, isFirstRow);
            }
        }
    }

    private int writeOnCellTrattenuteCliente(final int firstElement, final Sheet sheet, final List<ClienteViewModel> clienti)
            throws IllegalArgumentException, IllegalAccessException {
        int trattenuteWritten = 0;
        int rowNum = firstElement;
        for (final ClienteViewModel cliente : clienti) {
            if (cliente != null && cliente.getTrattenutaViewModelList() != null) {
                for (final TrattenuteViewModel trattenuta : cliente.getTrattenutaViewModelList()) {
                    final Row row = sheet.createRow(rowNum++);
                    final int colNum = 0;
                    writeTrattenuteCliente(sheet, trattenuta, row, colNum, cliente.getCf(),
                            cliente.getCognome(), cliente.getNome(), false);
                    trattenuteWritten++;
                }
            }
        }
        return trattenuteWritten;
    }

    private void writeFirstRowTrattenuteCliente(final Sheet sheet)
            throws IllegalArgumentException, IllegalAccessException {
        final Row row = sheet.createRow(0);
        // initialize first row with name of fields
        final int colNum = 0;
        writeTrattenuteCliente(sheet, null, row, colNum, CF, COGNOME, NOME, true);
    }

    /**
     * Foglio Estinzione Pratiche
     */

    private void writeEstinzioniPratiche(final Sheet sheet, final EstinzioneViewModel estinzione, final Row row,
                                         int colNum, final String idPratica, final boolean isFirstRow) throws IllegalArgumentException, IllegalAccessException {
        final Field[] estinzioneFields = getEstinzioneFields();
        final Cell cellCf = row.createCell(colNum++);
        cellCf.setCellValue(idPratica);
        for (final Field estinzioneField : estinzioneFields) {
            estinzioneField.setAccessible(true);
            if (!estinzioneField.getName().equalsIgnoreCase(VIEWID)) {
                final Cell cell = row.createCell(colNum++);
                chooseValueToWriteOnCell(estinzioneField, estinzione, row, colNum, cell, isFirstRow);
            }
        }
    }

    private int writeOnCellEstinzionePratiche(final int firstElement, final Sheet sheet, final List<ClienteViewModel> clienti)
            throws IllegalArgumentException, IllegalAccessException {
        int estinzioniWritten = 0;
        int rowNum = firstElement;
        for (final ClienteViewModel cliente : clienti) {
            if (cliente != null && cliente.getPraticaViewModelList() != null) {
                for (final PraticaViewModel pratica : cliente.getPraticaViewModelList()) {
                    if (pratica != null && pratica.getEstinzioneViewModelList() != null) {
                        for (final EstinzioneViewModel estinzione : pratica.getEstinzioneViewModelList()) {
                            final Row row = sheet.createRow(rowNum++);
                            final int colNum = 0;
                            writeEstinzioniPratiche(sheet, estinzione, row, colNum,
                                    Long.toString(pratica.getCodicePratica()), false);
                            estinzioniWritten++;
                        }
                    }
                }
            }
        }
        return estinzioniWritten;
    }

    private void writeFirstRowEstinzionePratiche(final Sheet sheet)
            throws IllegalArgumentException, IllegalAccessException {
        final Row row = sheet.createRow(0);
        // initialize first row with name of fields
        final int colNum = 0;
        writeEstinzioniPratiche(sheet, null, row, colNum, CODICEPRATICA, true);
    }

    private void chooseValueToWriteOnCell(final Field field, final Object clienteType, final Row row, final int colNum, final Cell cell,
                                          final boolean isFirstRow) throws IllegalArgumentException, IllegalAccessException {
        if (!isFirstRow) {
            if (field.get(clienteType) != null) {
                cell.setCellValue(field.get(clienteType).toString());
            }
        } else {
            cell.setCellValue(field.getName());
        }
    }

}
