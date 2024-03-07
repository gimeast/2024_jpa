/**
 * Submit form data as JSON to a specified URL and if successful, redirect the page
 * @param {string} actionUrl - URL to submit the data to
 * @param {string} redirectUrl - URL to redirect to on success
 * @param {HTMLFormElement} form - Form element to get the data from. If not specified, the first form in the document is used.
 */
async function submitForm(actionUrl, redirectUrl, form = document.querySelector('form')) {
    const data = new FormData(form);

    // FormData 객체를 JSON으로 변환
    const json = {};
    data.forEach((value, key) => json[key] = value);

    const response = await fetch(actionUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(json),
    });

    // 서버가 응답을 보냈을 때 처리, 예를 들면 페이지 이동 등
    if (response.status === 200) {
        location.href = redirectUrl;
    } else {
        console.error('Error:', response);
    }
}