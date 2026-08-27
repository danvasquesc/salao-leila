async function apiRequest(url, options = {}) {

    const config = {
        ...options,
        headers: {
            "Content-Type": "application/json",
            ...options.headers
        }
    };

    const response = await fetch(url, config);

    if (!response.ok) {

        const error =
            new Error(
                `Erro na requisição. Status: ${response.status}`
            );

        error.status = response.status;

        throw error;
    }

    if (response.status === 204) {
        return null;
    }

    return response.json();
}