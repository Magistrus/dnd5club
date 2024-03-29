package club.dnd5.portal.controller.api.wiki;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.Column;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.Search;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.FilterApi;
import club.dnd5.portal.dto.api.FilterValueApi;
import club.dnd5.portal.dto.api.wiki.RuleApi;
import club.dnd5.portal.dto.api.wiki.RuleDetailApi;
import club.dnd5.portal.dto.api.wiki.RuleRequestApi;
import club.dnd5.portal.model.rule.Rule;
import club.dnd5.portal.repository.datatable.RuleDatatableRepository;
import club.dnd5.portal.util.SpecificationUtil;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Rule", description = "The Rule API")
@RestController
public class RulesApiConroller {
	@Autowired
	private RuleDatatableRepository ruleRepository;
	
	@PostMapping(value = "/api/v1/rules", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<RuleApi> getRules(@RequestBody RuleRequestApi request) {
		Specification<Rule> specification = null;

		DataTablesInput input = new DataTablesInput();
		List<Column> columns = new ArrayList<Column>(3);
		Column column = new Column();
		column.setData("name");
		column.setName("name");
		column.setSearchable(Boolean.TRUE);
		column.setOrderable(Boolean.TRUE);
		column.setSearch(new Search("", Boolean.FALSE));
		columns.add(column);
		
		column = new Column();
		column.setData("englishName");
		column.setName("englishName");
		column.setSearch(new Search("", Boolean.FALSE));
		column.setSearchable(Boolean.TRUE);
		column.setOrderable(Boolean.TRUE);
		columns.add(column);
		
		column = new Column();
		column.setData("altName");
		column.setName("altName");
		column.setSearchable(Boolean.TRUE);
		column.setOrderable(Boolean.FALSE);

		columns.add(column);
		if (request.getOrders()!=null && !request.getOrders().isEmpty()) {
			specification = SpecificationUtil.getAndSpecification(specification, (root, query, cb) -> {
				List<Order> orders = request.getOrders().stream()
						.map(
							order -> "asc".equals(order.getDirection()) ? cb.asc(root.get(order.getField())) : cb.desc(root.get(order.getField()))
						)
						.collect(Collectors.toList());
				query.orderBy(orders);
				return cb.and();
			});
		}
		input.setColumns(columns);
		input.setLength(request.getLimit() != null ? request.getLimit() : -1);
		if (request.getPage() != null && request.getLimit()!=null) {
			input.setStart(request.getPage() * request.getLimit());	
		}
		if (request.getSearch() != null) {
			if (request.getSearch().getValue() != null && !request.getSearch().getValue().isEmpty()) {
				if (request.getSearch().getExact() != null && request.getSearch().getExact()) {
					specification = (root, query, cb) -> cb.equal(root.get("name"), request.getSearch().getValue().trim().toUpperCase());
				} else {
					input.getSearch().setValue(request.getSearch().getValue());
					input.getSearch().setRegex(Boolean.FALSE);
				}
			}
		}
		if (request.getFilter() != null) {
			if (!request.getFilter().getCategory().isEmpty()) {
				specification = SpecificationUtil.getAndSpecification(
					specification, (root, query, cb) -> root.get("type").in(request.getFilter().getCategory()));
			}
		}
		return ruleRepository.findAll(input, specification, specification, RuleApi::new).getData();
	}
	
	@PostMapping(value = "/api/v1/rules/{englishName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RuleDetailApi> getRule(@PathVariable String englishName) {
		Rule rule = ruleRepository.findByEnglishName(englishName.replace('_', ' '));
		if (rule == null) {
			ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(new RuleDetailApi(rule));
	}
	
	@PostMapping("/api/v1/filters/rules")
	public FilterApi getFilter() {
		FilterApi filters = new FilterApi();

		List<FilterApi> otherFilters = new ArrayList<>();
		
		FilterApi categoryFilter = new FilterApi("Категория", "category");
		categoryFilter.setValues(
				ruleRepository.findAllCategories().stream()
				 .map(value -> new FilterValueApi(value, value))
				 .collect(Collectors.toList()));
		otherFilters.add(categoryFilter);

		filters.setOther(otherFilters);
		return filters;
	}
}