// Variables to store webcam-related objects
let webcamStream;
let webcamVideo;
let webcamPopup;

function openWebcamPopup() {
    // Get the webcam popup element
    webcamPopup = document.getElementById('webcamPopup');

    // Get the webcam video element
    webcamVideo = document.getElementById('webcamVideo');

    // Check if the browser supports getUserMedia
    if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
        // Open the webcam popup
        webcamPopup.style.display = 'block';

        // Request access to the webcam
        navigator.mediaDevices.getUserMedia({ video: true })
            .then(function(stream) {
                // Save the webcam stream
                webcamStream = stream;

                // Attach the stream to the video element
                webcamVideo.srcObject = stream;
                webcamVideo.play();
            })
            .catch(function(error) {
                console.error('Error accessing webcam:', error);
            });
    } else {
        console.error('getUserMedia is not supported');
    }
}

function takePicture() {
    // Create a canvas element
    const canvas = document.createElement('canvas');
    canvas.width = webcamVideo.videoWidth;
    canvas.height = webcamVideo.videoHeight;

    // Draw the current video frame onto the canvas
    const context = canvas.getContext('2d');
    context.drawImage(webcamVideo, 0, 0, canvas.width, canvas.height);

    // Get the base64-encoded image data
    const imageData = canvas.toDataURL('image/png');

    // Do something with the image data, such as sending it to the server
    console.log('Image data:', imageData);
}

function closeWebcamPopup() {
    // Stop the webcam stream
    if (webcamStream) {
        webcamStream.getTracks().forEach(function(track) {
            track.stop();
        });
    }

    // Hide the webcam popup
    webcamPopup.style.display = 'none';
}
