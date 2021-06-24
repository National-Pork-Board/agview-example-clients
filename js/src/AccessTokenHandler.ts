import axios from 'axios';

export async function getAccessToken(baseUrl: string, apiKey: String, apiSecret: string) {

    return axios.post(baseUrl + '/auth/org-token/', {
        key: apiKey,
        secret: apiSecret
    })
        .then(res => res.data)
        .catch(error => {
            console.error(error)
        })
}
