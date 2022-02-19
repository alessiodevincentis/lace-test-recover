/**
 * Created by Emanuele on 09/03/2017.
 */
function initializeComuneNascitaAutocomplete() {
    // Create the autocomplete object, restricting the search
    // to geographical location types.
    var autocomplete = new google.maps.places.Autocomplete(
        (document.getElementById('form:comune')), {
            types: ['(cities)']
        });

    //save fields
    var comuneNascita = document.getElementById('form:comune');

    // When the user selects an address from the dropdown,
    // populate the address fields in the form.
    google.maps.event.addListener(autocomplete, 'place_changed', function() {
        //reset fields
        comuneNascita.value = '';

        // Get each component of the address from the place details
        // and fill the corresponding field on the form.
        var place = autocomplete.getPlace();
        for (var i = 0; i < place.address_components.length; i++) {
            var addressTypes = place.address_components[i].types;
            addressTypes.forEach(function (addressType) {
                var valueToInsert = place.address_components[i][componentForm[addressType]];
                switch (addressType) {
                    case "locality":
                        if(comuneNascita.value == '') {
                            comuneNascita.value = valueToInsert;
                        }
                        break;
                    case "administrative_area_level_3":
                        if(comuneNascita.value == '') {
                            comuneNascita.value = valueToInsert;
                        }
                        break;
                }
            })
        }
    });
}

$(window).load(function() {
    initializeComuneNascitaAutocomplete();
});