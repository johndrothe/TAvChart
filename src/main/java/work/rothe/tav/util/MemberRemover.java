package work.rothe.tav.util;

import work.rothe.tav.model.Document;
import work.rothe.tav.model.Member;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.function.Predicate.not;

public class MemberRemover {
    public static Document remove(Document document, int[] indexes) {
        return document.withMembers(removeMembers(document.members(), indexes));
    }

    private static List<Member> removeMembers(List<Member> members, int[] indexes) {
        return removeMembers(members, getSelected(members, indexes));
    }

    private static List<Member> removeMembers(List<Member> members, Set<Member> selected) {
        return members.stream().filter(not(selected::contains)).toList();
    }

    private static Set<Member> getSelected(List<Member> members, int[] indexes) {
        return IntStream.of(indexes)
                .filter(index -> index >= 0)
                .filter(index -> index < members.size())
                .mapToObj(members::get)
                .collect(Collectors.toSet());
    }
}
