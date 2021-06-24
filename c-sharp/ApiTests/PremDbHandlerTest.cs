using FluentAssertions;
using Npb.Agview.Api.Example;
using Xunit;

namespace Npb.Agview.ApiTests.Example
{

    public class PremDbHandlerTest
    {
        private readonly PremDbHandler _sut;
        private readonly string PremsFilePath = Constants.PremsFilePath;
        private readonly string PremAddressesFilePath = Constants.PremAddressesFilePath;

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
        private const string StreetAddress1 = "951 S. Green Rd";
        private const string City1 = "South Euclid";
        private const string Zip1 = "44121";
        private const string State1 = "OH";
        private const string StreetAddress2 = "555 Superior Ave";
        private const string City2 = "Cleveland";
        private const string Zip2 = "44108";
        private const string State2 = "OH";

        public PremDbHandlerTest()
        {
            _sut = new PremDbHandler(PremsFilePath, PremAddressesFilePath);
        }

        [Fact]
        public void GetsPrems()
        {
            var expected1 = new Prem
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

            var expected2 = new Prem
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

            var actual = _sut.GetPremsToLoad();

            actual.Should().HaveCount(2);
            actual.Should().ContainEquivalentOf(expected1);
            actual.Should().ContainEquivalentOf(expected2);
        }

        [Fact]
        public void GetsPremColumnNames()
        {
            var actual = _sut.GetPremColumnNames();

            string.Join(",", actual).Should().Be("usda_pin,prem_name,site_capacity_number_animals,ice_contact_email,ice_contact_phone,location_type,species,site_capacity_number_barns,number_of_animals_on_site");
        }

        [Fact]
        public void GetsPremAddresses()
        {
            var expected1 = new PremAddress
            {
                UsdaPin = UsdaPin1,
                StreetAddress = StreetAddress1,
                City = City1,
                Zip = Zip1,
                State = State1
            };
            var expected2 = new PremAddress
            {
                UsdaPin = UsdaPin2,
                StreetAddress = StreetAddress2,
                City = City2,
                Zip = Zip2,
                State = State2
            };

            var actual = _sut.GetPremAddressesToLoad();

            actual.Should().HaveCount(2);
            actual.Should().ContainEquivalentOf(expected1);
            actual.Should().ContainEquivalentOf(expected2);
        }

        [Fact]
        public void GetsPremAddressColumnNames()
        {
            var actual = _sut.GetPremAddressColumnNames();

            string.Join(",", actual).Should().Be("usda_pin,street_address,city,state,zip");
        }
    }

}
