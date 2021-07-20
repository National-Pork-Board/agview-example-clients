import axios from 'axios';

export async function getNewAccessToken(baseUrl: string, apiKey: String, apiSecret: string) {

    return axios.post(baseUrl + '/auth/org-token/', {
        key: apiKey,
        secret: apiSecret
    })
        .then(res => res.data)
        .catch(error => {
            console.error(error)
        })
}

export async function getNonExpiredOrNewAccessToken(baseUrl: string, apiKey: String, apiSecret: string,
    existingAccessToken: any, minimumValidityLeftInSeconds: number) {

    let expirationTimeInSeconds = existingAccessToken.exp

    if ((expirationTimeInSeconds - minimumValidityLeftInSeconds) >= (Date.now() / 1000)) {
        return existingAccessToken
    }

    return getNewAccessToken(baseUrl, apiKey, apiSecret)
}
