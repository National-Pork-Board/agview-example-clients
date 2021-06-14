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

        private const string UsdaPin1 = "1277XH9";
        private const string UsdaPin2 = "0615PKA";
        private const string PremName1 = "David's Prem";
        private const string PremName2 = "Nate's Prem";
        private const string IceContactEmail = "ice@vanila.com";
        private const string IceContactPhone = "12165551234";
        private const string LocationType = "Nursery";
        private const string Species = "Swine";
        private const int SiteCapacityNumberAnimals = 1000;
        private const int SiteCapacityNumberBarns = 3;
        private const int NumberOfAnimalsOnSite = 20;

        public PremiseDbHandlerTest()
        {
            _sut = new PremiseDbHandler(PremisesFilePath, PremiseAddressesFilePath);
        }

        [Fact]
        public void GetsPremises()
        {
            var expected1 = new Premise
            {
              UsdaPin = UsdaPin1,
              PremName = PremName1,
              SiteCapacityNumberAnimals = SiteCapacityNumberAnimals,
              IceContactEmail = IceContactEmail,
              IceContactPhone = IceContactPhone,
              LocationType = LocationType,
              Species = Species,
              SiteCapacityNumberBarns = SiteCapacityNumberBarns,
              NumberOfAnimalsOnSite = NumberOfAnimalsOnSite
            };

            var expected2 = new Premise
            {
                UsdaPin = UsdaPin2,
                PremName = PremName2,
                SiteCapacityNumberAnimals = SiteCapacityNumberAnimals,
                IceContactEmail = IceContactEmail,
                IceContactPhone = IceContactPhone,
                LocationType = LocationType,
                Species = Species,
                SiteCapacityNumberBarns = SiteCapacityNumberBarns,
                NumberOfAnimalsOnSite = NumberOfAnimalsOnSite
            };

            var actual = _sut.GetPremisesToLoad();

            actual.Should().HaveCount(2);
            actual.Should().ContainEquivalentOf(expected1);
            actual.Should().ContainEquivalentOf(expected2);
        }
    }
}
