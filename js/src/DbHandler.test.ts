import { getData } from './DbHandler'

const PREM_FILE_PATH = '../../db/prem.csv'

describe('DbHandler', () => {
    it('gets data', async () => {
        let actual = await getData(PREM_FILE_PATH)

        expect(actual.length).toEqual(2)
        expect(actual[0].get('usda_pin')).not.toBeNull()
        expect(actual[1].get('usda_pin')).not.toBeNull()
    });

    it('gets column names', async () => {
        let actual = await getData(PREM_FILE_PATH)
    });
})
