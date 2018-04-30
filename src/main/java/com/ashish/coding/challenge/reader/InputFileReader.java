package com.ashish.coding.challenge.reader;

import java.io.File;
import java.io.FileNotFoundException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.ashish.coding.challenge.parser.TransactionDataParser;

/**
 * The class is used as the Main Program for execution by Maven and command line
 * arguments. The complete path of the file needs to be provided.
 * @author ashishsharma
 *
 */
public class InputFileReader {

	public static void main(String[] args) {
		Options options = new Options();
		Option filePath = Option.builder("f")
				.longOpt("FilePath")
				.type(String.class)
				.hasArg()
				.required()
				.argName("File Path")
				.build();
		
		options.addOption(filePath);
		CommandLineParser commandLineParser = new DefaultParser();
		HelpFormatter helpFormatter = new HelpFormatter();
		String fileName ="";
		try {
			CommandLine commandLine = commandLineParser.parse(options, args, false);
			fileName = commandLine.getParsedOptionValue("FilePath").toString();
			System.out.println(fileName);
			File file = new File(fileName);
			if (file.exists() && file.isFile()) {
				TransactionDataParser parser = new TransactionDataParser();
				parser.parseTransactionData(fileName);
			} else {
				throw new FileNotFoundException("File Not Present. Please look below for correct usage.");
			}
		} catch (ParseException | FileNotFoundException e) {
			System.out.println(e.getMessage());
			helpFormatter.printHelp("Incorrect: Please enter Inputs as Below:", options);
			System.exit(1);
		} 

	}
}
