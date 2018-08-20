package io.gitlab.arturbosch.detekt.rules.style

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtClassLiteralExpression
import org.jetbrains.kotlin.psi.KtSimpleNameExpression
import org.jetbrains.kotlin.psi.psiUtil.getStrictParentOfType

/**
 * `Unit` should be used instead of `Void`. This rule detects and reports instances where
 * the `Void` type is used.
 *
 * <noncompliant>
 * runnable: () -> Void
 * var aVoid: Void? = null
 * </noncompliant>
 *
 * <compliant>
 * runnable: () -> Unit
 * Void::class
 * </compliant>
 *
 * @author Egor Neliuba
 */
class ForbiddenVoid(config: Config = Config.empty) : Rule(config) {

	override val issue = Issue(
			javaClass.simpleName,
			Severity.Style,
			"`Unit` should be used instead of `Void`.",
			Debt.FIVE_MINS)

	override fun visitSimpleNameExpression(expression: KtSimpleNameExpression) {
		if (expression.getReferencedName() == Void::class.java.simpleName && !expression.isClassLiteral) {
			report(CodeSmell(issue, Entity.from(expression), message = "'Void' should be replaced with 'Unit'."))
		}

		super.visitSimpleNameExpression(expression)
	}

	private val KtSimpleNameExpression.isClassLiteral: Boolean
		get() = getStrictParentOfType<KtClassLiteralExpression>() != null
}
