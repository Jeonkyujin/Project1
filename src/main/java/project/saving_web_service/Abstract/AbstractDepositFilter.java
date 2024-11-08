package project.saving_web_service.Abstract;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.persistence.TypedQuery;
import project.saving_web_service.domain.Deposit;
import project.saving_web_service.domain.Install;

public abstract class AbstractDepositFilter {

	public List<Deposit> mainFilter(String period, String amount, List<Deposit> deposit){
		List <Deposit> firstFilter = FilterByPeriod(deposit, period);
		List <Deposit> secondFilter = FilterByAmount(firstFilter, amount);

		return filterByCriteria(secondFilter);
	}

	protected abstract List<Deposit> filterByCriteria(List<Deposit> installments);
	public List<Deposit> FilterByPeriod(List<Deposit> deposit, String period){
		List<Deposit> firstFilterRecommendResult = new ArrayList<>();
		for (Deposit alldeposit : deposit) {
			if (alldeposit.get가입기간().contains("~")) {
				List<String> p = Arrays.asList(alldeposit.get가입기간().split("~"));
				if (p.size() >= 2) {
					if (!p.get(0).isEmpty()) {
						int start = Integer.parseInt(p.get(0).trim());
						int end = Integer.parseInt(p.get(1).trim());
						int a = Integer.parseInt(period);
						if (a >= start && a <= end) {
							firstFilterRecommendResult.add(alldeposit);
						}
					} else {
						int start = 0;
						int end = Integer.parseInt(p.get(1).trim());
						int a = Integer.parseInt(period);
						if (a >= start && a <= end) {
							firstFilterRecommendResult.add(alldeposit);
						}
					}

				} else {
					int start = Integer.parseInt(p.get(0).trim());
					int end = 100;
					int a = Integer.parseInt(period);
					if (a >= start && a <= end) {
						firstFilterRecommendResult.add(alldeposit);
					}
				}
			} else if (alldeposit.get가입기간().contains(",")) {
				List<String> p = Arrays.asList(alldeposit.get가입기간().split(","));
				if (p.contains(period)) {
					firstFilterRecommendResult.add(alldeposit);
				}
			} else if (alldeposit.get가입기간().equals("제한없음")) {
				firstFilterRecommendResult.add(alldeposit);

			} else {
				// 단일 값으로 되어 있을 경우 (예: 12, 24 등)
				try {
					// 가입기간 값을 int로 변환
					int singlePeriod = Integer.parseInt(alldeposit.get가입기간().trim());
					int a = Integer.parseInt(period);  // 입력받은 period 값도 int로 변환

					// 가입기간과 입력된 period가 동일한지 확인
					if (a == singlePeriod) {
						firstFilterRecommendResult.add(alldeposit);  // 동일하면 리스트에 추가
					}
				} catch (NumberFormatException e) {
					// 가입기간이 숫자로 변환되지 않는 경우 예외 처리 (잘못된 형식인 경우)
					System.out.println("잘못된 가입기간 형식: " + alldeposit.get가입기간());
				}
			}
		}
		return firstFilterRecommendResult;
	}

	private List<Deposit> FilterByAmount(List<Deposit> deposit, String amount){
		List<Deposit> secondFilterRecommendResult = new ArrayList<>();
		for (Deposit firstFilter : deposit) {
			if (firstFilter.get가입금액().contains("~")) {

				int a = Integer.parseInt(amount);
				List<String> price = Arrays.asList(firstFilter.get가입금액().split("~"));
				if (price.size() >= 2) {
					if (!price.get(0).isEmpty()) {
						double start = Double.parseDouble(price.get(0).trim());
						double end = Double.parseDouble(price.get(1).trim());

						if (a >= start && a <= end) {
							secondFilterRecommendResult.add(firstFilter);
						}
					} else {

						double end = Double.parseDouble(price.get(1).trim());
						if (a <= end) {
							secondFilterRecommendResult.add(firstFilter);
						}
					}

				} else {
					double start = Double.parseDouble(price.get(0).trim());
					int end = 10000;
					if (a >= start && a <= end) {
						secondFilterRecommendResult.add(firstFilter);
					}
				}
			} else if (firstFilter.get가입금액().equals("제한없음")) {
				secondFilterRecommendResult.add(firstFilter);
			}
		}
		return secondFilterRecommendResult;
	}

}

