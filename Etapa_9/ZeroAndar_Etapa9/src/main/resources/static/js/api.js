/** Etapa 9 - funções pequenas para chamar o Spring MVC pelo JavaScript. */
window.ZeroAndarApi = {
    request: function (url, options) {
        options = options || {};
        options.headers = Object.assign({'Content-Type': 'application/json'}, options.headers || {});
        return fetch(url, options).then(async function (response) {
            if (!response.ok) {
                var texto = await response.text();
                throw new Error(texto || 'Erro na comunicação com o servidor.');
            }
            if (response.status === 204) return null;
            return response.json();
        });
    },
    get: function (url) { return this.request(url); },
    post: function (url, data) { return this.request(url, {method:'POST', body:JSON.stringify(data)}); },
    put: function (url, data) { return this.request(url, {method:'PUT', body:JSON.stringify(data)}); },
    remove: function (url) { return this.request(url, {method:'DELETE'}); }
};
