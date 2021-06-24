using System.Collections.Generic;
using System.Globalization;
using System.IO;
using CsvHelper;

namespace Npb.Agview.Api.Example
{
    public class PremDbHandler
    {

        private readonly string _premsFilePath;
        private readonly string _premAddressesFilePath;

        public PremDbHandler(string premsFilePath, string premAddressesFilePath)
        {
            _premsFilePath = premsFilePath;
            _premAddressesFilePath = premAddressesFilePath;
        }

        public IEnumerable<Prem> GetPremsToLoad()
        {
            using var reader = new StreamReader(_premsFilePath);
            using var csv = new CsvReader(reader, CultureInfo.InvariantCulture);

            List<Prem> records = new();
            records.AddRange(csv.GetRecords<Prem>());

            return records;
        }

        public string[] GetPremColumnNames()
        {
            return GetColumnNames(_premsFilePath);
        }

        private string[] GetColumnNames(string filePath)
        {
            using var reader = new StreamReader(filePath);
            using var csv = new CsvReader(reader, CultureInfo.InvariantCulture);
            csv.Read();
            csv.ReadHeader();

            return csv.HeaderRecord;
        }

        public IEnumerable<PremAddress> GetPremAddressesToLoad()
        {
            using var reader = new StreamReader(_premAddressesFilePath);
            using var csv = new CsvReader(reader, CultureInfo.InvariantCulture);

            List<PremAddress> records = new();
            records.AddRange(csv.GetRecords<PremAddress>());

            return records;
        }

        public string[] GetPremAddressColumnNames()
        {

            return GetColumnNames(_premAddressesFilePath);
        }
    }
}
