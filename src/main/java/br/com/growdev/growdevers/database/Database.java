package br.com.growdev.growdevers.database;

import br.com.growdev.growdevers.models.Growdever;

import java.util.ArrayList;
import java.util.UUID;

public abstract class Database {
    private static ArrayList<Growdever> growdevers = new ArrayList<>();

    public static void addGrowdever(Growdever growdever){
        if(growdever.getId() == null){
            throw new RuntimeException("Growdever inválido");
        }
        growdevers.add(growdever);
    }

    public static void removeGrowdever(Growdever growdever) {
        if(growdever.getId() == null){
            throw new RuntimeException("Growdever inválido");
        }
        growdevers.remove(growdever);
    }

    public static ArrayList<Growdever> getGrowdevers(){
        return growdevers;
    }

    public static boolean growdeverExitsByCPF(String cpf){
        var growdeverFiltered = growdevers.stream().filter((growdever) -> growdever.getCpf().equals(cpf)).findAny();
        return growdeverFiltered.isPresent();
    }

    public static boolean growdeverExitsByEmail(String email){
        var growdeverFiltered = growdevers.stream().filter((growdever) -> growdever.getEmail().equalsIgnoreCase(email)).findAny();
        return growdeverFiltered.isPresent();
    }

    public static Growdever getGrowdeverById(UUID id){
        var growdeverFiltered = growdevers.stream().filter(growdever -> growdever.getId().equals(id)).findAny();

        if(growdeverFiltered.isEmpty()) return null;

        return growdeverFiltered.get();
    }

    public static void updateGrowdever(Growdever growdever){
        growdevers.removeIf(grow -> grow.getId().equals(growdever.getId()));
        growdevers.add(growdever);
    }
}
/*

[
{id: 1, name: "Edson", status: "ESTUDANDO"},
{id: 2, name: "Aryadne", status: "ESTUDANDO"}, // REMOVER
{id: 3, name: "Carla", status: "ESTUDANDO"},
]

{id: 2, name: "Aryadne Ronqui", status: "FORMADA"},

percorrer a lista e remover os objetos que possuem o mesmo ID


[
{id: 1, name: "Edson", status: "ESTUDANDO"},
{id: 3, name: "Carla", status: "ESTUDANDO"},
{id: 2, name: "Aryadne Ronqui", status: "FORMADA"},
]



 */
