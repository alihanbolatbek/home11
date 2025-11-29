import java.util.*;

class Candidate {
    private String name;

    public Candidate(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public void submitApplication() {
        System.out.println(name + " подал(а) заявку.");
    }

    public void attendInterview() {
        System.out.println(name + " проходит собеседование.");
    }

    public void receiveOffer() {
        System.out.println(name + " получил(а) оффер.");
    }

    public void confirmOffer() {
        System.out.println(name + " подтвердил(а) оффер.");
    }

    public void receiveRejection() {
        System.out.println(name + " получил(а) уведомление об отказе.");
    }
}


class Head {
    private String name;

    public Head(String name) {
        this.name = name;
    }

    public void createVacancyRequest() {
        System.out.println(name + " создал(а) заявку на вакансию.");
    }

    public void notifyRevision() {
        System.out.println(name + " получил(а) уведомление о доработке заявки.");
    }

    public void conductTechnicalInterview() {
        System.out.println(name + " проводит техническое собеседование.");
    }
}


class HR {
    private String name;

    public HR(String name) {
        this.name = name;
    }

    public boolean reviewRequest(boolean requestValid) {
        if (requestValid) {
            System.out.println(name + " утверждает заявку.");
            return true;
        } else {
            System.out.println(name + " отклоняет заявку.");
            return false;
        }
    }

    public List<Candidate> reviewApplications(List<ApplicationReview> applications) {
        List<Candidate> approved = new ArrayList<>();

        for (ApplicationReview app : applications) {
            Candidate candidate = app.candidate;
            if (app.isValid) {
                System.out.println(name + " приглашает " + candidate.getName() + " на собеседование.");
                approved.add(candidate);
            } else {
                System.out.println(name + " отклоняет заявку " + candidate.getName() + ".");
                candidate.receiveRejection();
            }
        }
        return approved;
    }

    public void conductInitialInterview(Candidate candidate) {
        System.out.println(name + " проводит первичное интервью " + candidate.getName() + ".");
    }
}


class ApplicationReview {
    Candidate candidate;
    boolean isValid;

    public ApplicationReview(Candidate candidate, boolean isValid) {
        this.candidate = candidate;
        this.isValid = isValid;
    }
}


class SystemCore {
    public void addEmployee(Candidate candidate) {
        System.out.println(candidate.getName() + " добавлен(а) в базу данных компании.");
    }

    public void notifyIT() {
        System.out.println("IT-отдел уведомлен о необходимости настройки рабочего места.");
    }
}


// ================= Пример процесса =================

public class Task2 {
    public static void main(String[] args) {

        Head head = new Head("Руководитель отдела");
        HR hr = new HR("HR-менеджер");
        SystemCore system = new SystemCore();

        Candidate candidate1 = new Candidate("Иван");
        Candidate candidate2 = new Candidate("Мария");

        // Подготовительный этап
        head.createVacancyRequest();

        if (hr.reviewRequest(true)) {

            // Этап подачи заявок
            candidate1.submitApplication();
            candidate2.submitApplication();

            List<ApplicationReview> applications = Arrays.asList(
                    new ApplicationReview(candidate1, true),
                    new ApplicationReview(candidate2, false)
            );

            List<Candidate> approvedCandidates = hr.reviewApplications(applications);

            // Этап собеседований
            for (Candidate cand : approvedCandidates) {
                hr.conductInitialInterview(cand);
                head.conductTechnicalInterview();

                boolean passedInterview = true;
                if (passedInterview) {
                    cand.receiveOffer();
                    cand.confirmOffer();
                    system.addEmployee(cand);
                    system.notifyIT();
                } else {
                    cand.receiveRejection();
                }
            }
        } else {
            head.notifyRevision();
        }
    }
}
