package io.gitlab.arturbosch.detekt.api

import java.nio.file.Files
import java.nio.file.Path

/**
 * @author Artur Bosch
 */
abstract class OutputReport : Extension {

	fun write(report: Path, detektion: Detektion) {
		val smellData = render(detektion)
		smellData?.let {
			report.parent?.let { Files.createDirectories(it) }
			Files.write(report, it.toByteArray())
		}
	}

	abstract fun render(detektion: Detektion): String?
}