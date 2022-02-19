/**
 * Created by Emanuele on 09/03/2017.
 */
function initializeIndirizzoResidenzaAutocomplete() {
    // Create the autocomplete object, restricting the search
    // to geographical location types.
    var autocomplete = new google.maps.places.Autocomplete(
        (document.getElementById('form:indresidenza')), {
            types: ['geocode']
        });

    //save fields
    var indirizzoResidenza = document.getElementById('form:indresidenza');
    var civicoResidenza = document.getElementById('form:civicoresidenza');
    var luogoResidenza = document.getElementById('form:luogoresidenza');
    var provinciaResidenza = document.getElementById('form:provresidenza');
    var capResidenza = document.getElementById('form:capresidenza');
    var nazioneResidenza = document.getElementById('form:nazioneresidenza');

    // When the user selects an address from the dropdown,
    // populate the address fields in the form.
    google.maps.event.addListener(autocomplete, 'place_changed', function() {
        //reset fields
        indirizzoResidenza.value = '';
        civicoResidenza.value = '';
        luogoResidenza.value = '';
        provinciaResidenza.value = '';
        capResidenza.value = '';
        nazioneResidenza.value = '';
        // Get each component of the address from the place details
        // and fill the corresponding field on the form.
        var place = autocomplete.getPlace();
        for (var i = 0; i < place.address_components.length; i++) {
            var addressTypes = place.address_components[i].types;
            addressTypes.forEach(function (addressType) {
                var valueToInsert = place.address_components[i][componentForm[addressType]];
                switch (addressType) {
                    case "route":
                        indirizzoResidenza.value= valueToInsert;
                        break;
                    case "street_number":
                        civicoResidenza.value= valueToInsert;
                        break;
                    case "locality":
                        if(luogoResidenza.value == '') {
                            luogoResidenza.value = valueToInsert;
                        }
                        break;
                    case "administrative_area_level_3":
                        if(luogoResidenza.value == '') {
                            luogoResidenza.value = valueToInsert;
                        }
                        break;
                    case "administrative_area_level_2":
                        provinciaResidenza.value= valueToInsert;
                        break;
                    case "postal_code":
                        capResidenza.value= valueToInsert;
                        break;
                    case "country":
                        nazioneResidenza.value = valueToInsert;
                        break;
                }
            })
        }
    });
}

function initializeFormsNominativoResidenzaAutocomplete() {
    initializeIndirizzoResidenzaAutocomplete();
}

$(window).load(function() {
    initializeFormsNominativoResidenzaAutocomplete();
});