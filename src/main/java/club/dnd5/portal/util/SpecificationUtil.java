package club.dnd5.portal.util;

import org.springframework.data.jpa.domain.Specification;

public final class SpecificationUtil {
	public static <T> Specification<T> getAndSpecification(Specification<T> specification, Specification<T> addSpecification) {
		if (specification == null) {
			return Specification.where(addSpecification);
		}
		return specification.and(addSpecification);
	}
	
	public static <T> Specification<T> getOrSpecification(Specification<T> specification, Specification<T> addSpecification) {
		if (specification == null) {
			return Specification.where(addSpecification);
		}
		return specification.or(addSpecification);
	}
}