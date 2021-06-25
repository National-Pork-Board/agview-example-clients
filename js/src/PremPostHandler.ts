import axios, { AxiosResponse } from 'axios';
import { getData } from "./DbHandler"
import { getNewAccessToken } from "./AccessTokenHandler"
import Constants from './Constants'

export async function createPrems(premFilePath: string, premAddressFilePath: string) {
    let prems = (await getData(premFilePath)).body
    let premAddresses = (await getData(premAddressFilePath)).body

    let premAddressByUsdaPin: Map<string, Map<string, any>> = new Map()
    premAddresses.forEach(premAddress => {
        premAddressByUsdaPin.set(premAddress.get('usda_pin'), premAddress)
    })

    let requestBody: Array<any> = createRequestBody(prems, premAddressByUsdaPin)

    let accessToken = await getNewAccessToken(Constants.NPB_BASE_URL, Constants.NPB_API_KEY, Constants.NPB_API_SECRET)

    return axios.post(`${Constants.NPB_BASE_URL}/api/v1/prems/`, requestBody,
        {
            headers: { Authorization: `Bearer ${accessToken.access}` }
        })
        .then((res: AxiosResponse<any>) => res)
        .catch(error => {
            console.error(error)
        })
}

function createRequestBody(prems: Map<string, any>[], premAddressByUsdaPin: Map<string, Map<string, any>>): Array<any> {
    let requestBody: Array<any> = []
    prems.forEach(prem => {
        let requestBodyEntry: any = {}
        requestBodyEntry['usdaPin'] = getOrNull(prem.get('usda_pin'))
        requestBodyEntry['premName'] = getOrNull(prem.get('prem_name'))
        requestBodyEntry['species'] = getOrNull(prem.get('species'))
        requestBodyEntry['iceContactPhone'] = getOrNull(prem.get('ice_contact_phone'))
        requestBodyEntry['iceContactEmail'] = getOrNull(prem.get('ice_contact_email'))
        requestBodyEntry['locationType'] = getOrNull(prem.get('location_type'))
        requestBodyEntry['siteCapacityNumberBarns'] = getOrNull(prem.get('site_capacity_number_barns'))
        requestBodyEntry['siteCapacityNumberAnimals'] = getOrNull(prem.get('site_capacity_number_animals'))
        requestBodyEntry['numberOfAnimalsOnSite'] = getOrNull(prem.get('number_of_animals_on_site'))
        requestBodyEntry['streetAddress'] = getOrNull(getCorrespondingAddressRecord(premAddressByUsdaPin, prem).get('street_address'))
        requestBodyEntry['city'] = getOrNull(getCorrespondingAddressRecord(premAddressByUsdaPin, prem).get('city'))
        requestBodyEntry['state'] = getOrNull(getCorrespondingAddressRecord(premAddressByUsdaPin, prem).get('state'))
        requestBodyEntry['zip'] = getOrNull(getCorrespondingAddressRecord(premAddressByUsdaPin, prem).get('zip'))

        requestBody.push(requestBodyEntry)
    })

    return requestBody
}

function getCorrespondingAddressRecord(premAddressByUsdaPin: Map<string, Map<string, any>>, prem: Map<string, any>): Map<string, any> {

    return premAddressByUsdaPin.get(prem.get('usda_pin'))!
}

function getOrNull(value: any) {
    return value ? value : null
}
