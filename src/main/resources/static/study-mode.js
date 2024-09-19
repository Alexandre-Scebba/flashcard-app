let currentIndex = 0;
let flashcards = /*[[${flashcards}]]*/ JSON.parse(/*[[${flashcards}]]*/ '[]');
let isFlipped = false;
let studyStartTime = Date.now();
let totalFlashcards = flashcards.length;

function showFlashcard(index) {
    const frontContent = document.getElementById('card-front-content');
    const backContent = document.getElementById('card-back-content');
    frontContent.textContent = flashcards[index].frontContent;
    backContent.textContent = flashcards[index].backContent;
    document.getElementById('flipCard').style.transform = 'rotateY(0deg)';
    isFlipped = false;
}

document.getElementById('flipButton').addEventListener('click', function () {
    const flipCard = document.getElementById('flipCard');
    if (isFlipped) {
        flipCard.style.transform = 'rotateY(0deg)';
    } else {
        flipCard.style.transform = 'rotateY(180deg)';
    }
    isFlipped = !isFlipped;
});

document.getElementById('previousCard').addEventListener('click', function () {
    if (currentIndex > 0) {
        currentIndex--;
        showFlashcard(currentIndex);
    }
});

document.getElementById('nextCard').addEventListener('click', function () {
    if (currentIndex < totalFlashcards - 1) {
        currentIndex++;
        showFlashcard(currentIndex);
    }
});

showFlashcard(currentIndex);

// Track study time and save progress when done
window.addEventListener('beforeunload', function () {
    const studyEndTime = Date.now();
    const timeSpent = (studyEndTime - studyStartTime) / 1000; // Time in seconds
    saveStudyProgress(currentIndex, timeSpent); // Save the current card and time spent
});

function saveStudyProgress(currentCardIndex, timeSpent) {
    // Send this data to your backend for saving in DynamoDB
    // AJAX or fetch request to save study progress
}
