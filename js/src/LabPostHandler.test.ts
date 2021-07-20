import { AxiosResponse } from 'axios';
import { createLab } from './LabPostHandler'

describe('PremPostHandler', () => {
    it('creates prems', async () => {
        let actual = (await createLab()) as AxiosResponse<string>

        expect(actual.data).toContain("<ACK_R25>")
    });
})
