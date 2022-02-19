/**
 * Created by Emanuele on 21/04/2017.
 */
function initializeLuogoNascitaCoobbligatoAutocomplete() {
    // Create the autocomplete object, restricting the search
    // to geographical location types.
    var autocomplete = new google.maps.places.Autocomplete(
        (document.getElementById('form:coobbligatoRepeat:0:luogoNascitaCoobbligato')), {
            types: ['(cities)']
        });

    //save fields
    var luogoNascita = document.getElementById('form:coobbligatoRepeat:0:luogoNascitaCoobbligato');
    var provinciaNascita = document.getElementById('form:coobbligatoRepeat:0:provinciaNascitaCoobbligato');
    var nazioneNascita = document.getElementById('form:coobbligatoRepeat:0:nazioneNascitaCoobbligato');

    // When the user selects an address from the dropdown,
    // populate the address fields in the form.
    google.maps.event.addListener(autocomplete, 'place_changed', function() {
        //reset fields
        luogoNascita.value = '';
        provinciaNascita.value = '';
        nazioneNascita.value = '';
        // Get each component of the address from the place details
        // and fill the corresponding field on the form.
        var place = autocomplete.getPlace();
        for (var i = 0; i < place.address_components.length; i++) {
            var addressTypes = place.address_components[i].types;
            addressTypes.forEach(function (addressType) {
                var valueToInsert = place.address_components[i][componentForm[addressType]];
                switch (addressType) {
                    case "locality":
                        if(luogoNascita.value == '') {
                            luogoNascita.value = valueToInsert;
                        }
                        break;
                    case "administrative_area_level_3":
                        if(luogoNascita.value == '') {
                            luogoNascita.value = valueToInsert;
                        }
                        break;
                    case "administrative_area_level_2":
                        provinciaNascita.value= valueToInsert;
                        break;
                    case "country":
                        nazioneNascita.value = valueToInsert;
                        break;
                }
            })
        }
    });
}

function initializeIndirizzoAbitazioneCoobbligatoAutocomplete() {
    // Create the autocomplete object, restricting the search
    // to geographical location types.
    var autocomplete = new google.maps.places.Autocomplete(
        (document.getElementById('form:coobbligatoRepeat:0:indirizzoAbitazioneCoobbligato')), {
            types: ['geocode']
        });

    //save fields
    var indirizzoResidenza = document.getElementById('form:coobbligatoRepeat:0:indirizzoAbitazioneCoobbligato');
    var civicoResidenza = document.getElementById('form:coobbligatoRepeat:0:civicoAbitazioneCoobbligato');
    var luogoResidenza = document.getElementById('form:coobbligatoRepeat:0:comuneAbitazioneCoobbligato');
    var provinciaResidenza = document.getElementById('form:coobbligatoRepeat:0:provinciaAbitazioneCoobbligato');
    var capResidenza = document.getElementById('form:coobbligatoRepeat:0:capAbitazioneCoobbligato');
    var nazioneResidenza = document.getElementById('form:coobbligatoRepeat:0:nazioneAbitazioneCoobbligato');

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

function initializeLuogoRilascioCoobbligatoAutocomplete() {
    // Create the autocomplete object, restricting the search
    // to geographical location types.
    var autocomplete = new google.maps.places.Autocomplete(
        (document.getElementById('form:coobbligatoRepeat:0:comuneRilascioCoobbligato')), {
            types: ['(cities)']
        });

    //save fields
    var luogoRilascio = document.getElementById('form:coobbligatoRepeat:0:comuneRilascioCoobbligato');
    var provinciaRilascio = document.getElementById('form:coobbligatoRepeat:0:provinciaRilascioCoobbligato');
    var nazioneRilascio = document.getElementById('form:coobbligatoRepeat:0:nazioneRilascioCoobbligato');

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


function initializeFormsClienteCoobbligatoAutocomplete() {
    initializeLuogoNascitaCoobbligatoAutocomplete();
    initializeIndirizzoAbitazioneCoobbligatoAutocomplete();
    initializeLuogoRilascioCoobbligatoAutocomplete();
}

$(window).load(function() {
    initializeFormsClienteCoobbligatoAutocomplete()
});