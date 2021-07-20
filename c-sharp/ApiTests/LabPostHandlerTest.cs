using System.Net.Http;
using System.Threading.Tasks;
using FluentAssertions;
using Npb.Agview.Api.Example;
using Xunit;

namespace Npb.Agview.ApiTests.Example
{
    public class LabPostHandlerTest
    {
        private readonly LabPostHandler _sut;

        private readonly HttpClient _httpClient = new();
        private readonly string BaseUrl = Constants.BaseUrl;
        private readonly string ApiKey = Constants.ApiKey;
        private readonly string ApiSecret = Constants.ApiSecret;

        public LabPostHandlerTest()
        {
            var accessTokenHandler = new AccessTokenHandler(_httpClient, BaseUrl, ApiKey, ApiSecret);

            _sut = new LabPostHandler(_httpClient, accessTokenHandler, BaseUrl);
        }

        [Fact]
        public async Task CreatesLab()
        {
            var actual = await _sut.CreateLab();

            actual.Should().Contain("<ACK_R25>");
        }
    }
}
