package org.example;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.CreateUserRequest;
import software.amazon.awssdk.services.iam.model.DeleteUserRequest;
import software.amazon.awssdk.services.iam.model.IamException;

public class App {
    public static void main(String[] args) {

        // Vérifier les arguments
        if (args.length < 1) {
            System.out.println("Usage: java -jar app.jar <create|delete|both> [userName]");
            System.out.println("Ex: java -jar app.jar create SDK-User-PIGIER");
            return;
        }

        String action = args[0].toLowerCase();
        String userName = args.length >= 2 ? args[1] : "SDK-User-PIGIER";

        // Créer le client IAM avec builder
        try (IamClient iam = IamClient.builder()
                .region(Region.US_EAST_1)  // ou prendre AWS_REGION des variables d'environnement
                .build()) {

            switch (action) {
                case "create":
                    createIAMUser(iam, userName);
                    break;
                case "delete":
                    deleteIAMUser(iam, userName);
                    break;
               
                default:
                    System.err.println("Action inconnue: " + action);
                    System.out.println("Actions disponibles: create, delete, both");
            }

        } catch (IamException e) {
            System.err.println("Erreur IAM : " + e.awsErrorDetails().errorMessage());
        }
    }

    // Méthode pour créer un utilisateur
    public static void createIAMUser(IamClient iam, String userName) {
        try {
            CreateUserRequest request = CreateUserRequest.builder()
                    .userName(userName)
                    .build();
            iam.createUser(request);
            System.out.println("Utilisateur créé : " + userName);
        } catch (IamException e) {
            System.err.println("Erreur lors de la création : " + e.awsErrorDetails().errorMessage());
        }
    }

    // Méthode pour supprimer un utilisateur
    public static void deleteIAMUser(IamClient iam, String userName) {
        try {
            DeleteUserRequest request = DeleteUserRequest.builder()
                    .userName(userName)
                    .build();
            iam.deleteUser(request);
            System.out.println("Utilisateur supprimé : " + userName);
        } catch (IamException e) {
            System.err.println("Erreur lors de la suppression : " + e.awsErrorDetails().errorMessage());
        }
    }
}
