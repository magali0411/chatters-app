# Projet chat-app

L'objectif de ce projet est de développer une application logicielle client / serveur qui permet aux gens de dialoguer sur internet, suivant plusieurs architectures conceptuelles. Ce chat est composé d'un serveur et d'une application cliente qui permettent respectivement de: 

- Gérer les connexions client
- Gérer une liste de pseudonymes d'utilisateurs
- Gérer la réception et le transfert des messages depuis et vers les applications client
- Obtenir une connexion sur le serveur
- Poster des messages sur le serveur
- Récupérer les messages du serveur

## 1 - Architecture push

Dans le cadre de ce projet, j'ai choisi de réaliser l'application en m'inspirant de l'architecture push : 

<img width="600" alt="push architecture" src="img/push.png">


Pour se connecter, chaque client possède une _ClientApp_. Chacune d'entre elles instancie :

- Un composant de réception __(Receiver)__ qui permet de recevoir la liste des clients connectés ainsi que les messages reçus depuis l'application serveur (messagerie instantannée)
- Un composant d'émission __(Emietter)__ qui est instancié pour être utilisé par le client pour envoyer des messages au serveur 



Un composant de connection __(Connection)__ propre au serveur permet  d'attribuer à chaque application cliente un emietter et de stocker la référence du proxy, associée au  pseudo du Client dans une  structure de données HashMap.



## 2 - Fonctionnement du chat



Mon projet se découpe en deux grosses parties : la partie Serveur et le partie Client. L'interface graphique a été réalisé avec la librairie Javafx (version 11.0.2) . 

La classe serveur permet de lancer un serveur local sur le port 1099 et de créer un registre RMI. 

Pour lancer l'application cliente, il faut lancer la classe ClientApp qui étend la classe Application de javafx. Celle-ci récupère une instance Connection avec le registre RMI du port 1099. 

#### Lancement du serveur 

> ```
> janv. 01, 2021 11:57:45 AM main.java.serveur.Server main
> INFO: Serveur actif
> ```

#### Lancement d'application clientes

<img width="400" alt="client app" src="img/roger.png">

<img width="300" alt="port 109" src="img/fail.png">




L'application ne se lance que si tout les champs sont complets et un serveur écoute bien sur le port sur serveur précisé. Dans l'exemple, l'utilisateur à essayé de se connecter au __port 109__ au lieu du __1099__ et ne peut donc pas se connecter. 

Trois clients connectés sur le __port 1099__ du serveur _locahost_ :

<img width="800" alt="connections des clients" src="img/3apps.png">


La liste des clients connectés se met automatiquement à jour. Chaque client peut alors envoyer des messages aux autres clients connectés. 

Les messages ne s'envoient qu'aux clients concernés. Si le message est vide ou que le destinataire n'est pas connecté, le message n'est pas envoyé. 

<img width="800" alt="messages" src="img/messages.png">

Un utilisateur a la possibilité de supprimer les message s'affichant sur son interface. Si un client se déconnecte, sa fenêtre de chat se ferme et son nom disparait instantanément de la liste des clients connectés. 


<img width="600" alt="deconnection" src="img/2apps.png">


Grâce à l'utilisation de Logger, on peut observer le comportement de l'application dans la console : 

```
janv. 01, 2021 12:11:20 PM main.java.client.ClientApp lambda$chatScene$0
INFO: name : Julie
janv. 01, 2021 12:11:21 PM main.java.serveur.ReceiverImpl addClient
INFO: Client ajoutÃ© : Julie
INFO: Stage is closing
```
