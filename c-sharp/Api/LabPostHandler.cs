using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;

namespace Npb.Agview.Api.Example
{
    public class LabPostHandler
    {
        private readonly HttpClient _httpClient;
        private readonly AccessTokenHandler _accessTokenHandler;
        private readonly string _baseUrl;

        public LabPostHandler(HttpClient httpClient,
            AccessTokenHandler accessTokenHandler,
            string baseUrl)
        {
            _httpClient = httpClient;
            _accessTokenHandler = accessTokenHandler;
            _baseUrl = baseUrl;
        }

        public async Task<string> CreateLab()
        {
            var requestBodyStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"  standalone=\"yes\"?><OPU_R25><MSH><MSH.1>|</MSH.1><MSH.2>^~\\&amp;</MSH.2><MSH.3><HD.1>VADDS</HD.1><HD.2>2.16.840.1.113883.3.5.1.33</HD.2><HD.3>ISO</HD.3></MSH.3><MSH.4><HD.1>004BF57</HD.1><HD.2>2.16.840.1.113883.3.5.1.33</HD.2><HD.3>ISO</HD.3></MSH.4><MSH.6><HD.1>0034P2K</HD.1><HD.2>2.16.840.1.113883.3.5.6.1.4</HD.2><HD.3>ISO</HD.3></MSH.6><MSH.7>20210514152514-0700</MSH.7><MSH.9><MSG.1>OPU</MSG.1><MSG.2>R25</MSG.2><MSG.3>OPU_R25</MSG.3></MSH.9><MSH.10>  2135000515-20210514152514</MSH.10><MSH.11><PT.1>P</PT.1></MSH.11><MSH.12><VID.1>2.6</VID.1></MSH.12><MSH.21><EI.1>NAHLNResultBase_1_0_9_5</EI.1><EI.3>2.16.840.1.113883.5.9</EI.3><EI.4>ISO</EI.4></MSH.21></MSH><PV1><PV1.2>C</PV1.2><PV1.19><CX.1>21-35000515-11064</CX.1><CX.4><HD.2>2.16.840.1.113883.3.5.1.33</HD.2><HD.3>ISO</HD.3></CX.4></PV1.19></PV1><ROL><ROL.2>UC</ROL.2><ROL.3><CWE.1>SUB</CWE.1><CWE.2>Submitter</CWE.2><CWE.3>HL70443</CWE.3></ROL.3><ROL.4><XCN.1>ARC004</XCN.1><XCN.2><FN.1>ARC004</FN.1></XCN.2><XCN.3>  58320 </XCN.3><XCN.9><HD.2>2.16.840.1.113883.3.5.1.33</HD.2><HD.3>ISO</HD.3></XCN.9></ROL.4><ROL.11><XAD.1><SAD.1>79 N. Franklin Tpk.</SAD.1></XAD.1><XAD.3>BROOKINGS</XAD.3><XAD.4>SD</XAD.4><XAD.5>57006</XAD.5></ROL.11><ROL.12><XTN.2>WPN</XTN.2><XTN.3>PH</XTN.3><XTN.12>(605) 999-2222</XTN.12></ROL.12></ROL><ROL><ROL.2>UC</ROL.2><ROL.3><CWE.1>PREM</CWE.1><CWE.2>Source Premises</CWE.2><CWE.3>HL70443</CWE.3></ROL.3><ROL.4><XCN.2><FN.1>NA</FN.1></XCN.2></ROL.4><ROL.11><XAD.4>SD</XAD.4></ROL.11><ROL.13><PL.10><EI.1>4839YRF</EI.1><EI.3>2.16.840.1.113883.3.5.6.1.1</EI.3><EI.4>ISO</EI.4></PL.10></ROL.13></ROL><ROL><ROL.2>UC</ROL.2><ROL.3><CWE.1>PAR</CWE.1><CWE.2>Parent Company</CWE.2><CWE.3>HL70443</CWE.3></ROL.3><ROL.4><XCN.2><FN.1>ARC001</FN.1></XCN.2></ROL.4></ROL><OPU_R25.ACCESSION_DETAIL><NK1><NK1.1>1</NK1.1><NK1.2><XPN.1><FN.1> ANYTOWN VET CLINIC</FN.1></XPN.1></NK1.2></NK1><OPU_R25.PATIENT><PID><PID.3><CX.1></CX.1></PID.3><PID.5><XPN.1><FN.1>NA</FN.1></XPN.1></PID.5><PID.8>U</PID.8><PID.35><CWE.1>78678003</CWE.1><CWE.2>PORCINE</CWE.2><CWE.3>SCT</CWE.3></PID.35><PID.38><CWE.1>MT</CWE.1><CWE.2>Meat</CWE.2><CWE.3>HL70429</CWE.3></PID.38></PID></OPU_R25.PATIENT><OPU_R25.SPECIMEN><SPM><SPM.2><EIP.1><EI.1>{}</EI.1><EI.3>2.16.840.1.113883.3.5.8.4</EI.3><EI.4>ISO</EI.4></EIP.1><EIP.2><EI.1>21-35000515-126772</EI.1><EI.3>2.16.840.1.113883.3.5.1.33</EI.3><EI.4>ISO</EI.4></EIP.2></SPM.2><SPM.4><CWE.1>257261003</CWE.1><CWE.2>SWAB</CWE.2><CWE.3>SCT</CWE.3></SPM.4><SPM.11><CWE.1>P</CWE.1><CWE.2>Patient</CWE.2><CWE.3>HL70369</CWE.3></SPM.11><SPM.17><DR.1>20210101 000000-0700</DR.1></SPM.17><SPM.18>20210514000000-0700</SPM.18></SPM><OPU_R25.ORDER><OBR><OBR.3><EI.1>86945</EI.1><EI.3>2.16.840.1.113883.3.5.1.33</EI.3><EI.4>ISO</EI.4></OBR.3><OBR.4><CWE.1>1111111</CWE.1><CWE.2>TEST</CWE.2><CWE.3>LN</CWE.3></OBR.4><OBR.22>20210514152451-0700</OBR.22><OBR.31><CWE.1>N</CWE.1><CWE.2>National Surveillance</CWE.2><CWE.3>L</CWE.3></OBR.31></OBR><ORC><ORC.1>NW</ORC.1><ORC.4><EI.3> 2.16.840.1.113883.3.5.8.7 </EI.3><EI.4>ISO</EI.4></ORC.4><ORC.5>CM</ORC.5></ORC><OPU_R25.RESULT><OBX><OBX.2>NM</OBX.2><OBX.3><CWE.1>1111111</CWE.1><CWE.2>TEST</CWE.2><CWE.3>LN</CWE.3></OBX.3><OBX.5><CWE.1>260385009</CWE.1><CWE.2>NOT DETECTED</CWE.2><CWE.3>SCT</CWE.3><CWE.9> </CWE.9></OBX.5><OBX.8>NDET</OBX.8><OBX.11>F</OBX.11><OBX.19>20210514152451-0500</OBX.19></OBX></OPU_R25.RESULT></OPU_R25.ORDER></OPU_R25.SPECIMEN></OPU_R25.ACCESSION_DETAIL></OPU_R25>";

            var accessToken = _accessTokenHandler.GetNewAccessToken().Result;
            _httpClient.DefaultRequestHeaders.Authorization =
                    new AuthenticationHeaderValue("Bearer", accessToken.Access);

            var response = await _httpClient.PostAsync($"{_baseUrl}/api/v1/labs/", new StringContent(requestBodyStr, Encoding.UTF8, "application/xml"));

            return response.Content.ReadAsStringAsync().Result;
        }
    }
}
