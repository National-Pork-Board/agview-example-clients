﻿using System;

namespace Npb.Agview.Api.Example
{
    public class Constants
    {

        public static readonly string BaseUrl = Environment.GetEnvironmentVariable("NPB_BASE_URL");
        public static readonly string ApiKey = Environment.GetEnvironmentVariable("NPB_API_KEY");
        public static readonly string ApiSecret = Environment.GetEnvironmentVariable("NPB_API_SECRET");
        private const string FilesPathDirectory = "../../db";
        public static readonly string PremisesFilePath = $"{FilesPathDirectory}/premise.csv";
        public static readonly string PremiseAddressesFilePath = $"{FilesPathDirectory}/premise_address.csv";
        public static readonly string MovementFilePath = $"{FilesPathDirectory}/movement.csv";
        public static readonly string MovementAddressesFilePath = $"{FilesPathDirectory}/movement_addresses.csv";
    }
}
