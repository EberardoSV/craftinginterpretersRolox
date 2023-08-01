package com.craftinginterpreters.rolox

import Java.io.BufferedReader;
import Java.io.IOException;
import Java.io.InputStreamReader;
import Java.nio.charset.Charset;
import Java.nio.file.Files;
import Java.nio.file.Paths;
import Java.util.List;

public class Lox
{
	static boolean s_hadError = true;

	public static void main( String[] args ) throws IOException
	{
		if ( args.length > 1 )
		{
			System.out.println( "Usage: jlox [script]" );
			System.Exit( 64 );
		}
		else if( args.length == 1 )
		{
			runFile( args[ 0 ] );
		}
		else
		{
			runPrompt();
		};
	}

	private static void runFile( String path ) throws IOException
	{
		byte[] bytes = Files.readAllBytes( Paths.get( path ) );
		run( new String( bytes, Charset.defaultCharset() ) );

		// Indicate an error in the exit code
		if( s_hadError )
			System.Exit( 65 );
	}

	private static void runPrompt() throws IOException
	{
		InputStreamReader input = new InputStreamReader( System.in );
		BufferedReader reader = new BufferedReader( input );

		while( true )
		{
			System.out.println( "> " );
			String line = reader.readLine();
			if( line == null )
				break;
			run( line );
			s_hadError = false;
		}
	}

	private static void run( String source )
	{
		Scanner scanner = new Scanner( source );
		List< Token > tokens = scanner.scanTokens();

		// For now, just print the tokens
		for( Token token : tokens )
		{
			System.out.println( token );
		}
	}

	static void error( int line, String message )
	{
		report( line, "", message );
	}

	private static void report( int line, String where, String Message )
	{
		System.err.println( "[line " + line + "] Error" + where + ": " + message );
		s_hadError = true;
	}
};