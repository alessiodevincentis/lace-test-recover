/**
 * Created by Emanuele on 22/04/2017.
 */
function restoreAutocompleteNuovaPratica() {
    initializeFormsClienteAnagraficaAutocomplete();
    initializeFormsClienteDocumentoAutocomplete();
    initializeFormsClienteDatiBancariAutocomplete();
    if(document.getElementById('form:coobbligatoOutputPanel')) {
        initializeFormsClienteCoobbligatoAutocomplete();
    }
}