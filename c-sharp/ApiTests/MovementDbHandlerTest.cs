using FluentAssertions;
using Npb.Agview.Api.Example;
using Xunit;

namespace Npb.Agview.ApiTests.Example
{

    public class MovementDbHandlerTest
    {
        private readonly MovementDbHandler _sut;

        private readonly string MovementFilePath = TestConstants.MovementFilePath;
        private readonly string MovementAddressesFilePath = TestConstants.MovementAddressesFilePath;

        private const int MovementId1 = 1;
        private const string Species = "Swine";
        private const int NumberInShipment = 10;
        private const string MovementDateTime = "2021-06-08T13:45";
        private const string MovementType = "Feeder Pigs";

        private const string SourcePremId = "1277XH9";
        private const string TargetPremId = "0615PKA";

        public MovementDbHandlerTest()
        {
            _sut = new MovementDbHandler(MovementFilePath, MovementAddressesFilePath);
        }

        [Fact]
        public void GetsMovements()
        {
            var expectedMovement = new Movement {
                MovementId = MovementId1,
                Species = Species,
                NumberInShipment = NumberInShipment,
                MovementDatetime = MovementDateTime,
                MovementType = MovementType
            };

            var actual = _sut.GetMovementsToLoad();

            actual.Should().ContainEquivalentOf(expectedMovement);
        }

        [Fact]
        public void GetsMovementColumnNames()
        {
            var actual = _sut.GetMovementColumnNames();

            string.Join(",", actual).Should().Be("movement_id,species,number_in_shipment,movement_datetime,movement_type");
        }

        [Fact]
        public void GetsMovementAddresses()
        {
            var expectedAddress = new MovementAddresses
            {
                MovementId = MovementId1,
                Source = SourcePremId,
                Target = TargetPremId
            };

            var actual = _sut.GetMovementAddressesToLoad();

            actual.Should().ContainEquivalentOf(expectedAddress);
        }

        [Fact]
        public void GetsPremiseAddressColumnNames()
        {
            var actual = _sut.GetMovementAddressesColumnNames();

            string.Join(",", actual).Should().Be("movement_id,source,source_latitude,source_longitude,source_street_address,source_city,source_state,source_zip,destination,destination_latitude,destination_longitude,destination_street_address,destination_city,destination_state,destination_zip");
        }

    }

}
