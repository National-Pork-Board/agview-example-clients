using System.Collections.Generic;
using System.Globalization;
using System.IO;
using CsvHelper;

namespace Npb.Agview.Api.Example
{
    public class MovementDbHandler
    {
        private readonly string _movementFilePath;
        private readonly string _movementAddressesFilePath;

        public MovementDbHandler(string movementFilePath, string movementAddressesFilePath)
        {
            _movementFilePath = movementFilePath;
            _movementAddressesFilePath = movementAddressesFilePath;
        }

        public IEnumerable<Movement> GetMovementsToLoad()
        {
            using var reader = new StreamReader(_movementFilePath);
            using var csv = new CsvReader(reader, CultureInfo.InvariantCulture);

            List<Movement> records = new();
            records.AddRange(csv.GetRecords<Movement>());

            return records;
        }

        public string[] GetMovementColumnNames()
        {
            return GetColumnNames(_movementFilePath);
        }

        private string[] GetColumnNames(string filePath)
        {
            using var reader = new StreamReader(filePath);
            using var csv = new CsvReader(reader, CultureInfo.InvariantCulture);
            csv.Read();
            csv.ReadHeader();

            return csv.HeaderRecord;
        }

        public IEnumerable<MovementAddresses> GetMovementAddressesToLoad()
        {
            using var reader = new StreamReader(_movementAddressesFilePath);
            using var csv = new CsvReader(reader, CultureInfo.InvariantCulture);
            csv.Context.TypeConverterOptionsCache.GetOptions<string>().NullValues.Add("");

            List<MovementAddresses> records = new();
            records.AddRange(csv.GetRecords<MovementAddresses>());

            return records;
        }

        public string[] GetMovementAddressesColumnNames()
        {

            return GetColumnNames(_movementAddressesFilePath);
        }
    }
}
