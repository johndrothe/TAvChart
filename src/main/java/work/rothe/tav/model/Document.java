package work.rothe.tav.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.With;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

@With
public record Document(String name,
                       int borderHour,
                       Dimension canvasSize,
                       List<Member> members)
{
    @JsonCreator
    public Document(@JsonProperty("name") String name,
                    @JsonProperty("borderHour") int borderHour,
                    @JsonProperty("canvasSize") Dimension canvasSize,
                    @JsonProperty("members") List<Member> members) {
        this.name = name;
        this.borderHour = borderHour;
        this.canvasSize = canvasSize;
        this.members = List.copyOf(members);
    }

    public Document(String name, int borderHour, List<Member> members) {
        this(name, borderHour, new Dimension(1280,1024), members);
    }

    public Document(String name, List<Member> members) {
        this(name, 0, members);
    }

    public static Document fromCsv(String fileName, String csvContents) {
        if (csvContents.isEmpty()) {
            return null;
        }
        return new Document(toDocumentName(fileName), 0, membersFromCsv(csvContents));
    }

    private static List<Member> membersFromCsv(String csvContents) {
        return Arrays.stream(csvContents.split("\n"))
                .skip(1)//column headers
                .map(row -> row.split(", ?"))
                .map(Member::fromCsv)
                .toList();
    }

    private static String toDocumentName(String fileName) {
        if (fileName.toLowerCase().endsWith(".csv")) {
            return fileName.substring(0, fileName.length() - 4);
        }
        return fileName;
    }

    @JsonIgnore
    public List<Zone> zones() {
        return Stream.concat(defaultZones(), memberZones()).distinct().toList();
    }

    private static Stream<Zone> defaultZones() {
        return Stream.of(Zone.utc());
    }

    private Stream<Zone> memberZones() {
        return members.stream().map(Member::zone);
    }

    public String toCsv() {
        return members().stream()
                .map(Member::toCsv)
                .collect(joining("\n", csvHeader(), "\n"));
    }

    public Document withUpdatedMember(Member oldMember, Member newMember) {
        return withMembers(replaceMember(new ArrayList<>(members), oldMember, newMember));
    }

    private static List<Member> replaceMember(List<Member> list, Member oldMember, Member newMember) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == oldMember) {
                list.set(i, newMember);
                return List.copyOf(list);
            }
        }
        throw new IllegalArgumentException("No such member found in member list.");
    }

    private String csvHeader() {
        return "Name, Role, Location, Zone ID, Normal Start, Normal End, Lunch Start, Lunch End\n";
    }
}
