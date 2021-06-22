import axios from 'axios';

export function getAccessToken(baseUrl: string, apiKey: String, apiSecret: string) {
    axios.post(baseUrl + '/auth/org-token/', {
        key: apiKey,
        secret: apiSecret
    })
        .then(res => {
            console.log(res)
        })
        .catch(error => {
            console.error(error)
        })
}
