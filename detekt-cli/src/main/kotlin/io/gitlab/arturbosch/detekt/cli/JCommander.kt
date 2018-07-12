package io.gitlab.arturbosch.detekt.cli

import com.beust.jcommander.JCommander
import com.beust.jcommander.ParameterException

inline fun <reified T : Args> parseArguments(args: Array<String>): T {
	val cli = T::class.java.declaredConstructors.firstOrNull()?.newInstance() as? T
			?: throw IllegalStateException("Could not create Args object for class ${T::class.java}")

	val jCommander = JCommander()

	jCommander.addObject(cli)
	jCommander.programName = "detekt"

	try {
		jCommander.parse(*args)
	} catch (ex: ParameterException) {
		val message = ex.message
		failWithErrorMessages(message)
	}

	if (cli.help) {
		jCommander.usage()
		System.exit(0)
	}

	return cli
}

fun failWithErrorMessages(vararg messages: String?) {
	failWithErrorMessages(messages.asIterable())
}

fun failWithErrorMessages(messages: Iterable<String?>) {
	messages.forEach {
		println(it)
	}
	println()
	JCommander().usage()
	System.exit(-1)
}
