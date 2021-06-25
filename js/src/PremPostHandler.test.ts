import { AxiosResponse } from 'axios';
import { createPrems } from './PremPostHandler'

const PREM_FILE_PATH = '../../db/prem.csv'
const PREM_ADDRESS_FILE_PATH = '../../db/prem_address.csv'

describe('PremPostHandler', () => {
    it('creates prems', async () => {
        let actual = (await createPrems(PREM_FILE_PATH, PREM_ADDRESS_FILE_PATH)) as AxiosResponse<any>

        expect(actual.data).toEqual('')
    });
})
