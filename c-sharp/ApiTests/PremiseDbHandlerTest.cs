using FluentAssertions;
using Npb.Agview.Api.Example;
using Xunit;

namespace Npb.Agview.ApiTests.Example
{

    public class PremiseDbHandlerTest
    {
        private readonly PremiseDbHandler _sut;
        private const string PremisesFilePath = Constants.PremisesFilePath;
        private const string PremiseAddressesFilePath = Constants.PremiseAddressesFilePath;

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

        [Fact]
        public void GetsPremiseColumnNames()
        {
            var actual = _sut.GetPremiseColumnNames();

            string.Join(",", actual).Should().Be("usda_pin,prem_name,site_capacity_number_animals,ice_contact_email,ice_contact_phone,location_type,species,site_capacity_number_barns,number_of_animals_on_site");
        }

        [Fact]
        public void GetsPremiseAddresses()
        {
            var expected1 = new PremiseAddress
            {
                UsdaPin = UsdaPin1,
                StreetAddress = StreetAddress1,
                City = City1,
                Zip = Zip1,
                State = State1
            };
            var expected2 = new PremiseAddress
            {
                UsdaPin = UsdaPin2,
                StreetAddress = StreetAddress2,
                City = City2,
                Zip = Zip2,
                State = State2
            };

            var actual = _sut.GetPremiseAddressesToLoad();

            actual.Should().HaveCount(2);
            actual.Should().ContainEquivalentOf(expected1);
            actual.Should().ContainEquivalentOf(expected2);
        }

        [Fact]
        public void GetsPremiseAddressColumnNames()
        {
            var actual = _sut.GetPremiseAddressColumnNames();

            string.Join(",", actual).Should().Be("usda_pin,street_address,city,state,zip");
        }
    }

}
