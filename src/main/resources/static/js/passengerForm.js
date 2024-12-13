 document.addEventListener('DOMContentLoaded', function () {
        var modeInput = document.getElementById('modeHiddenInput');
        var passengerIdInput = document.getElementById('idInput');

        var UpdatedPassengerId = document.getElementById('hiddenPassengerId');
        if (UpdatedPassengerId) {
            showUpdateSuccessAndRedirect(UpdatedPassengerId.value);
        }

    if(modeInput){
        const form = document.getElementById('passengerForm');
            var mode = modeInput.value;
            if(passengerIdInput){
              var passengerId = passengerIdInput.value;
            }
            else{
               console.log("PassengerId not found in the DOM");
            }



        if (mode === 'add') {
//            passengerIdInput.style.display = 'none';
            form.action = '/bookMyBus/addPassenger';
            form.method = 'post' ;
               }

        if (mode === 'update') {
            passengerIdInput.setAttribute('readonly', true); // Make the field readonly
            passengerIdInput.style.backgroundColor = 'lightgray';
            form.action = '/bookMyBus/passengerPortal/' + passengerId + '/update';
            form.method = 'post' ;
        }
        if (mode === 'view') {
             var inputs = document.querySelectorAll('input');
            // Disable each input
                inputs.forEach(function(input) {
//                    console.log("Disabling input: " + input.name);  // Debugging each input
                    input.disabled = true;
                });

            var textAreas = document.querySelectorAll('textarea');
                    textAreas.forEach(function(textarea) {
//                        console.log("Disabling textarea: " + textarea.name);  // Debugging each textarea
                        textarea.disabled = true;
                        });

            form.method = 'get';
            form.action = '/bookMyBus/passengerPortal/' + passengerId;
        }

    }else {
          console.log("Mode not found in the DOM");
      }

});
    // Function to show the success notification and redirect
    function showUpdateSuccessAndRedirect(passengerId) {

        // Show the notification box
        document.getElementById('notificationBox').style.display = 'block';

        // Hide notification  after a few seconds (e.g., 3 seconds)  and then redirect
        setTimeout(function () {
            window.location.href = "/bookMyBus/passengerPortal/" + passengerId;
        }, 5000);  // 5000 milliseconds = 5 seconds
    }