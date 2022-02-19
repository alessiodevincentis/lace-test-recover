function initializeIndirizzoSedeLegaleAutocomplete() {
    // Create the autocomplete object, restricting the search
    // to geographical location types.
    var autocomplete = new google.maps.places.Autocomplete(
        (document.getElementById('form:indirizzosedelegale')), {
            types: ['geocode']
        });

    //save fields
    var indirizzoSedeLegale= document.getElementById('form:indirizzosedelegale');
    var civicoSedeLegale = document.getElementById('form:civicosedelegale');
    var luogoSedeLegale = document.getElementById('form:luogosedelegale');
    var provinciaSedeLegale = document.getElementById('form:provsedelegale');
    var capSedeLegale = document.getElementById('form:capsedelegale');
    var nazioneSedeLegale = document.getElementById('form:nazionesedelegale');

    // When the user selects an address from the dropdown,
    // populate the address fields in the form.
    google.maps.event.addListener(autocomplete, 'place_changed', function() {
        //reset fields
    	indirizzoSedeLegale.value = '';
    	civicoSedeLegale.value = '';
    	luogoSedeLegale.value = '';
    	provinciaSedeLegale.value = '';
    	capSedeLegale.value = '';
    	nazioneSedeLegale.value = '';
        // Get each component of the address from the place details
        // and fill the corresponding field on the form.
        var place = autocomplete.getPlace();
        for (var i = 0; i < place.address_components.length; i++) {
            var addressTypes = place.address_components[i].types;
            addressTypes.forEach(function (addressType) {
                var valueToInsert = place.address_components[i][componentForm[addressType]];
                switch (addressType) {
                    case "route":
                    	indirizzoSedeLegale.value= valueToInsert;
                        break;
                    case "street_number":
                    	civicoSedeLegale.value= valueToInsert;
                        break;
                    case "locality":
                        if(luogoSedeLegale.value == '') {
                        	luogoSedeLegale.value = valueToInsert;
                        }
                        break;
                    case "administrative_area_level_3":
                        if(luogoSedeLegale.value == '') {
                        	luogoSedeLegale.value = valueToInsert;
                        }
                        break;
                    case "administrative_area_level_2":
                    	provinciaSedeLegale.value= valueToInsert;
                        break;                         
                    case "postal_code":
                    	capSedeLegale.value= valueToInsert;
                        break;
                    case "country":
                    	nazioneSedeLegale.value = valueToInsert;
                        break;
                }
            })
        }
    });
}

function initializeIndirizzoSedeNotificaAutocomplete() {
    // Create the autocomplete object, restricting the search
    // to geographical location types.
    var autocomplete = new google.maps.places.Autocomplete(
        (document.getElementById('form:indirizzosedenotifica')), {
            types: ['geocode']
        });

    //save fields
    var indirizzoSedeNotifica= document.getElementById('form:indirizzosedenotifica');
    var civicoSedeNotifica = document.getElementById('form:civicosedenotifica');
    var luogoSedeNotifica = document.getElementById('form:luogosedenotifica');
    var provinciaSedeNotifica = document.getElementById('form:provsedenotifica');
    var capSedeNotifica = document.getElementById('form:capsedenotifica');
    var nazioneSedeNotifica = document.getElementById('form:nazionesedenotifica');

    // When the user selects an address from the dropdown,
    // populate the address fields in the form.
    google.maps.event.addListener(autocomplete, 'place_changed', function() {
        //reset fields
    	indirizzoSedeNotifica.value = '';
    	civicoSedeNotifica.value = '';
    	luogoSedeNotifica.value = '';
    	provinciaSedeNotifica.value = '';
    	capSedeNotifica.value = '';
    	nazioneSedeNotifica.value = '';
        // Get each component of the address from the place details
        // and fill the corresponding field on the form.
        var place = autocomplete.getPlace();
        for (var i = 0; i < place.address_components.length; i++) {
            var addressTypes = place.address_components[i].types;
            addressTypes.forEach(function (addressType) {
                var valueToInsert = place.address_components[i][componentForm[addressType]];
                switch (addressType) {
                    case "route":
                    	indirizzoSedeNotifica.value= valueToInsert;
                        break;
                    case "street_number":
                    	civicoSedeNotifica.value= valueToInsert;
                        break;
                    case "locality":
                        if(luogoSedeNotifica.value == '') {
                        	luogoSedeNotifica.value = valueToInsert;
                        }
                        break;
                    case "administrative_area_level_3":
                        if(luogoSedeNotifica.value == '') {
                        	luogoSedeNotifica.value = valueToInsert;
                        }
                        break;
                    case "administrative_area_level_2":
                    	provinciaSedeNotifica.value= valueToInsert;
                        break;                        
                    case "postal_code":
                    	capSedeNotifica.value= valueToInsert;
                        break;
                    case "country":
                    	nazioneSedeNotifica.value = valueToInsert;
                        break;
                }
            })
        }
    });
}

function initializeFormsAmministrazioneAutocomplete() {
    initializeIndirizzoSedeNotificaAutocomplete();
    initializeIndirizzoSedeLegaleAutocomplete();
}

$(window).load(function() {
    initializeFormsAmministrazioneAutocomplete();
});