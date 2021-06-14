using System;
using System.IO;
using System.Reflection;
using FluentAssertions;
using Npb.Agview.Api.Example;
using Xunit;

namespace Npb.Agview.ApiTests.Example
{

    

    public class PremiseDbHandlerTest
    {
        private readonly PremiseDbHandler _sut;
        private const string PremisesFilePath = "../../../../../db/premise.csv";
        private const string PremiseAddressesFilePath = "../../../../../db/premise_address.csv";

        public PremiseDbHandlerTest()
        {
            _sut = new PremiseDbHandler(PremisesFilePath, PremiseAddressesFilePath);
        }

        [Fact]
        public void GetsPremises()
        {
            var actual = _sut.GetPremisesToLoad();

            actual.Should().HaveCount(2);
        }
    }
}
