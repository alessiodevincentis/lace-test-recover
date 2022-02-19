/**
 * Created by Emanuele on 10/03/2017.
 */
function initializeIndirizzoBancaAutocomplete() {
    // Create the autocomplete object, restricting the search
    // to geographical location types.
    new google.maps.places.Autocomplete(
        (document.getElementById('form:sportello')), {
            types: ['geocode']
        });

}

function initializeFormsClienteDatiBancariAutocomplete() {
    initializeIndirizzoBancaAutocomplete();
}

$(window).load(function() {
    initializeFormsClienteDatiBancariAutocomplete();
});