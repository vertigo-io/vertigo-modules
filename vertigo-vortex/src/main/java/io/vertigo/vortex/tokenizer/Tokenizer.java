package io.vertigo.vortex.tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import io.vertigo.core.lang.Assertion;

/**
 * This tokenizer splits a text into tokens to read a 'command grammar'.
 * To be fast, furious and simple, only one character is necessary to identify the type of tokens.
 * 
 * All token types are defined by their first character
 * Quotes, brackets must be balanced
 * EOF is equivalent to an EOL 
 *  ____________________________________________________________________________________
 * Blanks :
 * 		- a sequence of one or more  : space, tab, new line
 *  ____________________________________________________________________________________
 * Comments :
 * 		- single line: begins with #, ends at the end of line -EOL- or at the end of file -EOF-
 * 		- This kind of comment can easily be cut and pasted
 *  ____________________________________________________________________________________
 * Separators : 
 * 		- all symbolic separators are defined in only ONE character
 * 		Punctuation :
 * 			- colon 		: ':'
 * 			- semi-colon 	: ';' 
 * 			- comma 		: ','
 * 
 * 		Brackets :
 * 			- brackets must be balanced
 * 			- 4 types of brackets :
 * 				- curly brackets 	{}
 * 				- round brackets 	()
 * 				- square brackets 	[]
 * 				- angle brackets 	<>
 *  ____________________________________________________________________________________
 * Keywords : 
 * 		- keywords use kebab syntax 
 * 		- keywords are lower case and used '-' as a word separator
 *		- some keywords are reserved such as booleans and must not be confused with a word !	
 * 		- begins with [a-z] 
 * 		- contains [a-z] or '-' 
 * 		- must end with [a-z]
 * 		- must be declared in a single line
 * 
 * 		- ex : toto 		# is a keyword
 * 		- ex : toto-titi	# is a keyword
 * 		- ex : toto-		# is NOT a valid keyword
 * 		- ex : true 		# is NOT a keyword but a boolean
 *  ____________________________________________________________________________________
 * Identifiers : 
 * 		- keywords use upper camel case syntax 
 * 		- identifiers are used to identify a structure 
 * 		- identifiers are case sensitive
 * 		- begins with [A-Z]
 * 		- contains [a-z] or [A-Z] or [0-9]
 * 		- must be declared in a single line
 * 
 * 		- ex : Toto 	# is an identifier
 * 		- ex : TotoTiti # is an identifier
 * 		- ex : Toto12 	# is an identifier
 *  ____________________________________________________________________________________
 * Literals :
 * 		Identifiers :
 * 			- the identifier of a structure can be used as a literal
 *			- see above
 *			
 * 		String :
 * 			- begins AND ends with a double quote : '"'
 * 			- The double quote character has to be escaped with a backslash '\'
 * 			- The backslash must be duplicated in a String
 * 			- Quotes must be balanced
 * 			- must be declared in a single line
  * 
 * 			- ex : "toto" => toto
 * 			- ex : "to\\to" => to\to
 * 			- ex : "to\"to" => to"to
 *
 *		<TODO>
 * 			- begins AND ends with """ for multiline String with escape character
 * 			- ex : """select name 
 * 						from user
 *						where id = xx""" 
 * 		</TODO>			
 * 
 * 		Integer :
 * 			- begins AND ends with a digit [0-9] or '-' and a digit 
 * 			- must be declared in a single line ( EOL or EOF is a separator)
 * 			- you can use many 0 at the begin of an integer	
 * 			- ex : 56
 * 			- ex : 056
 * 			- ex : 00056
 * 			- ex : -56
 * 			- ex : -056  
 * 			- ex : -00056  
 * 
 * 		Boolean :
 * 			- true or false (case sensitive)
 * 			- a boolean is a reserved word and must not be confused with a word !	
 * 			- ex : true
 * 			- ex : trueliness is a word and not a boolean !
 *  ____________________________________________________________________________________
 *  
 *  <TODO>
 *  ____________________________________________________________________________________
 * Pre-processing 
 * 	transforms source with some directives to include/exclude parts of text or include some other sources with conditions.
 * 	these directives can be viewed as pre-processor commands
 * 	+ variables 
 *  + directives to allow pre-processing of source
 *  
 * 		Variables :
 * 			- is a path of simple keys 
 * 			- begins with '$'
 * 			- contains the pattern /[a-z]+  that can be repeated
 * 			- must be declared in a single line ( EOL or EOF is a separator)
 * 			- ex : $/test/hidden # is a variable
 * 	
 * 		Directives :
 * 			- begins with / 
 * 			- contains [a-z] and '-' as a separator ( snake-case)
 * 			- must be declared in a single line ( EOL or EOF is a separator)
 * 			- ex : /set  # is a directive
 * 		</TODO>
 * 
 * @author pchretien
 */
public final class Tokenizer {
	private final List<TokenType> tokenTypes;

	public Tokenizer(List<TokenType> tokenTypes) {
		Assertion.check().isNotNull(tokenTypes);
		//---
		this.tokenTypes = tokenTypes;
	}

	public List<Token> tokenize(final String src) {
		final List<Token> tokens = new ArrayList<>();
		Matcher matcher;
		int pos = 0;
		while (pos < src.length()) {
			boolean match = false;
			for (TokenType tokenType : tokenTypes) {
				matcher = tokenType.pattern.matcher(src);
				matcher.region(pos, src.length());
				if (matcher.lookingAt()) {
					match = true;
					String tok = matcher.group();
					pos = matcher.end();
					tokens.add(new Token(tokenType, tok));
					break;
				}
			}
			if (!match)
				throw new ParserException("Unexpected character in input(" + src.length() + "): " + src);
		}
		return tokens;
	}

}