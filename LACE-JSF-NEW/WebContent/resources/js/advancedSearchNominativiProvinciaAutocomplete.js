function initializeProvinciaResidenzaAutocompleteNominativo() {
    // Create the autocomplete object, restricting the search
    // to geographical location types.
    var autocomplete = new google.maps.places.Autocomplete(
        (document.getElementById('form:advancedsearchnominativo:provinciaNominativo')), {
            types: ['(cities)']
        });

    //save fields
    var provinciaResidenza = document.getElementById('form:advancedsearchnominativo:provinciaNominativo');

    // When the user selects an address from the dropdown,
    // populate the address fields in the form.
    google.maps.event.addListener(autocomplete, 'place_changed', function () {
        //reset fields
        provinciaResidenza.value = '';
        // Get each component of the address from the place details
        // and fill the corresponding field on the form.
        var place = autocomplete.getPlace();
        for (var i = 0; i < place.address_components.length; i++) {
            var addressTypes = place.address_components[i].types;
            addressTypes.forEach(function (addressType) {
                var valueToInsert = place.address_components[i][componentForm[addressType]];
                switch (addressType) {
                    case "locality":
                        break;
                    case "administrative_area_level_3":
                        break;
                    case "administrative_area_level_2":
                        provinciaResidenza.value = valueToInsert;
                        break;
                    case "country":
                        break;
                }
            })
        }
    });
}

function initializeProvinciaResidenzaAutocompleteAdvancedSearchNominativo() {
    initializeProvinciaResidenzaAutocompleteNominativo();
}

$(window).load(function () {
    initializeProvinciaResidenzaAutocompleteAdvancedSearchNominativo();
});