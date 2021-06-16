using System;
namespace Npb.Agview.ApiTests.Example
{
    public class TestConstants
    {
        private const string FilesPathDirectory = "../../../../../db";
        public static readonly string PremisesFilePath = $"{FilesPathDirectory}/premise.csv";
        public static readonly string PremiseAddressesFilePath = $"{FilesPathDirectory}/premise_address.csv";
        public static readonly string MovementFilePath = $"{FilesPathDirectory}/movement.csv";
        public static readonly string MovementAddressesFilePath = $"{FilesPathDirectory}/movement_addresses.csv";
    }
}
