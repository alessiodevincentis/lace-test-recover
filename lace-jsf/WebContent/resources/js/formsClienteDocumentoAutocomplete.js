/**
 * Created by Emanuele on 09/03/2017.
 */
function initializeLuogoRilascioAutocomplete() {
    // Create the autocomplete object, restricting the search
    // to geographical location types.
    var autocomplete = new google.maps.places.Autocomplete(
        (document.getElementById('form:luogorilascio')), {
            types: ['(cities)']
        });

    //save fields
    var luogoRilascio = document.getElementById('form:luogorilascio');
    var provinciaRilascio = document.getElementById('form:provinciarilascio');
    var nazioneRilascio = document.getElementById('form:nazionerilascio');

    // When the user selects an address from the dropdown,
    // populate the address fields in the form.
    google.maps.event.addListener(autocomplete, 'place_changed', function() {
        //reset fields
        luogoRilascio.value = '';
        provinciaRilascio.value = '';
        nazioneRilascio.value = '';
        // Get each component of the address from the place details
        // and fill the corresponding field on the form.
        var place = autocomplete.getPlace();
        for (var i = 0; i < place.address_components.length; i++) {
            var addressTypes = place.address_components[i].types;
            addressTypes.forEach(function (addressType) {
                var valueToInsert = place.address_components[i][componentForm[addressType]];
                switch (addressType) {
                    case "locality":
                        if(luogoRilascio.value == '') {
                            luogoRilascio.value = valueToInsert;
                        }
                        break;
                    case "administrative_area_level_3":
                        if(luogoRilascio.value == '') {
                            luogoRilascio.value = valueToInsert;
                        }
                        break;
                    case "administrative_area_level_2":
                        provinciaRilascio.value= valueToInsert;
                        break;
                    case "country":
                        nazioneRilascio.value = valueToInsert;
                        break;
                }
            })
        }
    });
}


function initializeFormsClienteDocumentoAutocomplete() {
    initializeLuogoRilascioAutocomplete();
}

$(window).load(function() {
    initializeFormsClienteDocumentoAutocomplete();
});