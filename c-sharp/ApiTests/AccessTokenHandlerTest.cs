using System.Net.Http;
using System.Threading.Tasks;
using FluentAssertions;
using Npb.Agview.Api.Example;
using Xunit;

namespace Npb.Agview.ApiTests.Example
{
    public class AccessTokenHandlerTest
    {
        private readonly AccessTokenHandler _sut;
        private readonly HttpClient _httpClient = new();
        private readonly string BaseUrl = Constants.BaseUrl;
        private readonly string ApiKey = Constants.ApiKey;
        private readonly string ApiSecret = Constants.ApiSecret;
        private const int TenSeconds = 10;
        private const int TenThousandSeconds = 10000;

        public AccessTokenHandlerTest()
        {
            _sut = new AccessTokenHandler(_httpClient, BaseUrl, ApiKey, ApiSecret);
        }

        [Fact]
        public async Task GetsNewAccessToken()
        {
            var actual = await _sut.GetNewAccessToken();

            actual.Access.Should().NotBeNull();
            actual.Exp.Should().BeGreaterThan(0);
        }

        [Fact]
        public async Task ReusesExistingNonexpiredToken()
        {
            var originalAccessToken = await _sut.GetNewAccessToken();

            var actual = await _sut.GetNonExpiredOrNewAccessToken(originalAccessToken, TenSeconds);

            actual.Should().BeSameAs(originalAccessToken);
        }

        [Fact]
        public async Task GetsNewAccessTokenWhenExistingOneIsOutsideOfValidityRange()
        {
            var originalAccessToken = await _sut.GetNewAccessToken();

            var actual = await _sut.GetNonExpiredOrNewAccessToken(originalAccessToken, TenThousandSeconds);

            actual.Should().NotBeSameAs(originalAccessToken);
        }
    }
}
