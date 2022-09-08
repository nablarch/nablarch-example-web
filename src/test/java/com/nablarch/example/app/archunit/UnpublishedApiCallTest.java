package com.nablarch.example.app.archunit;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaMethodCall;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import nablarch.core.util.annotation.Published;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.tngtech.archunit.core.domain.AccessTarget.MethodCallTarget;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

public class UnpublishedApiCallTest {

	private static final JavaClasses javaClasses =
			new ClassFileImporter()
					.withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
					.importPackages("com.nablarch.example");

	@Test
	@DisplayName("使用不許可APIを呼び出していないこと")
	void test() {
		methods()
				//.that(notType(ClientAction.class, AuthenticationAction.class))
				.should(notCallNablarchUnpublishedApi).check(javaClasses);
	}


	private final ArchCondition<JavaMethod> notCallNablarchUnpublishedApi =	new NablarchUnpublishedApiCallArchCondition();

	private static class NablarchUnpublishedApiCallArchCondition extends ArchCondition<JavaMethod> {

		public NablarchUnpublishedApiCallArchCondition() {
			super("not call Nablarch Unpublished API");
		}

		@Override
		public void check(JavaMethod item, ConditionEvents events) {
			for (JavaMethodCall javaMethodCall : item.getMethodCallsFromSelf()) {
				MethodCallTarget calledMethod = javaMethodCall.getTarget();
				if (!isValidApiCall(calledMethod)) {
					events.add(SimpleConditionEvent.violated(
							item,
							"unpublished API call [" +	calledMethod.getFullName() + " ("
							+ item.getOwner().getSimpleName() + ".java:" + javaMethodCall.getSourceCodeLocation().getLineNumber() + ")")
					);
				}
			}
		}

		private boolean isValidApiCall(MethodCallTarget calledMethod) {
			JavaClass calledClass = calledMethod.getOwner();

			if (!calledClass.getPackageName().startsWith("nablarch")) {
				return true; // Nablarch APIでなはいので対象外
			}
			return calledClass.isAnnotatedWith(Published.class) || calledMethod.isAnnotatedWith(Published.class);
		}

	}

	public static DescribedPredicate<JavaMethod> notType(Class<?>... allowedClasses) {

		return new DescribedPredicate<JavaMethod>("") {
			@Override
			public boolean apply(JavaMethod input) {
				JavaClass owner = input.getOwner();
				return Arrays.stream(allowedClasses).noneMatch(owner::isAssignableFrom);
			}
		};
	}

}
