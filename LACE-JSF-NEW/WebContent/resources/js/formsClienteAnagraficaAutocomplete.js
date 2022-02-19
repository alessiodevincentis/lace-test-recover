/**
 * Created by Emanuele on 09/03/2017.
 */
function initializeLuogoNascitaAutocomplete() {
    // Create the autocomplete object, restricting the search
    // to geographical location types.
    var autocomplete = new google.maps.places.Autocomplete(
        (document.getElementById('form:luogonascita')), {
            types: ['(cities)']
        });

    //save fields
    var luogoNascita = document.getElementById('form:luogonascita');
    var provinciaNascita = document.getElementById('form:provnascita');
    var nazioneNascita = document.getElementById('form:nazionenascita');

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

function initializeIndirizzoDomicilioAltroAutocomplete() {
    // Create the autocomplete object, restricting the search
    // to geographical location types.
    var autocomplete = new google.maps.places.Autocomplete(
        (document.getElementById('form:domicilioaltro')), {
            types: ['geocode']
        });

    //save fields
    var indirizzoDomicilio = document.getElementById('form:domicilioaltro');
    var civicoDomicilio = document.getElementById('form:civicodomicilioaltro');
    var luogoDomicilio = document.getElementById('form:luogodomicilioaltro');
    var provinciaDomicilio = document.getElementById('form:provdomicilioaltro');
    var capDomicilio = document.getElementById('form:capdomicilioaltro');
    var nazioneDomicilio = document.getElementById('form:nazionedomicilioaltro');

    // When the user selects an address from the dropdown,
    // populate the address fields in the form.
    google.maps.event.addListener(autocomplete, 'place_changed', function() {
        //reset fields
        indirizzoDomicilio.value = '';
        civicoDomicilio.value = '';
        luogoDomicilio.value = '';
        provinciaDomicilio.value = '';
        capDomicilio.value = '';
        nazioneDomicilio.value = '';
        // Get each component of the address from the place details
        // and fill the corresponding field on the form.
        var place = autocomplete.getPlace();
        for (var i = 0; i < place.address_components.length; i++) {
            var addressTypes = place.address_components[i].types;
            addressTypes.forEach(function (addressType) {
                var valueToInsert = place.address_components[i][componentForm[addressType]];
                switch (addressType) {
                    case "route":
                        indirizzoDomicilio.value= valueToInsert;
                        break;
                    case "street_number":
                        civicoDomicilio.value= valueToInsert;
                        break;
                    case "locality":
                        if(luogoDomicilio.value == '') {
                            luogoDomicilio.value = valueToInsert;
                        }
                        break;
                    case "administrative_area_level_3":
                        if(luogoDomicilio.value == '') {
                            luogoDomicilio.value = valueToInsert;
                        }
                        break;
                    case "administrative_area_level_2":
                        provinciaDomicilio.value= valueToInsert;
                        break;
                    case "postal_code":
                        capDomicilio.value= valueToInsert;
                        break;
                    case "country":
                        nazioneDomicilio.value = valueToInsert;
                        break;
                }
            })
        }
    });
}

function initializeIndirizzoPrecedenteAutocomplete() {
    // Create the autocomplete object, restricting the search
    // to geographical location types.
    var autocomplete = new google.maps.places.Autocomplete(
        (document.getElementById('form:indirizzoprec')), {
            types: ['geocode']
        });

    //save fields
    var indirizzoPrecedente = document.getElementById('form:indirizzoprec');
    var civicoPrecedente = document.getElementById('form:civicoprec');
    var luogoPrecedente = document.getElementById('form:luogoprec');
    var provinciaPrecedente = document.getElementById('form:provprec');
    var capPrecedente = document.getElementById('form:capprec');
    var nazionePrecedente = document.getElementById('form:nazioneprec');

    // When the user selects an address from the dropdown,
    // populate the address fields in the form.
    google.maps.event.addListener(autocomplete, 'place_changed', function() {
        //reset fields
        indirizzoPrecedente.value = '';
        civicoPrecedente.value = '';
        luogoPrecedente.value = '';
        provinciaPrecedente.value = '';
        capPrecedente.value = '';
        nazionePrecedente.value = '';
        // Get each component of the address from the place details
        // and fill the corresponding field on the form.
        var place = autocomplete.getPlace();
        for (var i = 0; i < place.address_components.length; i++) {
            var addressTypes = place.address_components[i].types;
            addressTypes.forEach(function (addressType) {
                var valueToInsert = place.address_components[i][componentForm[addressType]];
                switch (addressType) {
                    case "route":
                        indirizzoPrecedente.value= valueToInsert;
                        break;
                    case "street_number":
                        civicoPrecedente.value= valueToInsert;
                        break;
                    case "locality":
                        if(luogoPrecedente.value == '') {
                            luogoPrecedente.value = valueToInsert;
                        }
                        break;
                    case "administrative_area_level_3":
                        if(luogoPrecedente.value == '') {
                            luogoPrecedente.value = valueToInsert;
                        }
                        break;
                    case "administrative_area_level_2":
                        provinciaPrecedente.value= valueToInsert;
                        break;
                    case "postal_code":
                        capPrecedente.value= valueToInsert;
                        break;
                    case "country":
                        nazionePrecedente.value = valueToInsert;
                        break;
                }
            })
        }
    });
}

function initializeFormsClienteAnagraficaAutocomplete() {
    initializeLuogoNascitaAutocomplete();
    initializeIndirizzoResidenzaAutocomplete();
    initializeIndirizzoDomicilioAltroAutocomplete();
    initializeIndirizzoPrecedenteAutocomplete();
}

$(window).load(function() {
    initializeFormsClienteAnagraficaAutocomplete()
});