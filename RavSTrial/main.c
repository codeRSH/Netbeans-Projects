/* 
 * File:   main.c
 * Author: ravs
 *
 * Created on July 13, 2010, 2:01 AM
 */



#include<stdio.h>
#include<stdlib.h>
#include<stdbool.h>

#define START 0
#define LAST 1

struct list {
    int data; //Data of linked list.
    struct list *next; // Pointer to next node of the list.
} *start = NULL; // Global variable. Poor programming.

void addNode(int ), delNode(int ), showNodes(void);

int main() {
    do {
        printf("\n\n");
        printf("\n Choose one option : \n");
        printf("\n 0. Exit");
        printf("\n 1. Add a node (at the start)");
        printf("\n 2. Add a node (at the last)");
        printf("\n 3. Delete a node (at the start)");
        printf("\n 4. Delete a node (at the end)");
        printf("\n 5. Show Linked List");
        printf("\n");

        int ch;
        scanf("%d", &ch); // Better input method?

        switch (ch) {
            case 0:
                printf("\n Exiting now... Press any key\n");
                // getch() not available and getchar() doesn't seem to work
                getchar();
                printf("\n");
                exit(0);
            case 1:
                addNode(START);
                break;
            case 2:
                addNode(LAST);
                break;
            case 3:
                delNode(START);
                break;
            case 4:
                delNode(LAST);
                break;
            case 5:
                showNodes();
                break;
            default:
                printf("\n You entered wrong option.. Try again.");
                printf("\n Press any key\n");
                getchar();
                break;
        }
    } while (true);

    return EXIT_SUCCESS; // Not required?
}

void addNode(int endToAdd) {

    // Conversion in C required or not?
    struct list *newNode = (struct list*) malloc(sizeof (struct list));
    if (newNode == NULL) {
        printf("\n Cannot create new node.. Exiting..");
        return;
    }

    printf("\nEnter data for new node:\n");

    int data;
    scanf("%d", &data);
    newNode->data = data;
    newNode->next = NULL;

    if (start == NULL) { // If list is empty
        start = newNode;
    }
    else {
        if (endToAdd == START) {
            newNode->next = start;
            start = newNode;
        }
        else if (endToAdd == LAST) {
            // Search for present last node and append new node.
            struct list *ptr = start;
            while (ptr) {
                if (ptr->next == NULL) {
                    ptr->next = newNode;
                    break;
                }
                ptr = ptr->next;
            }
        }
    }

    printf("\n Node added. \n\n");
}

void delNode(int endToDelete) {
    if (start == NULL) {
        printf("\n Underflow...");
        getchar();
        return;
    }

    struct list *ptr = start;
    if (endToDelete == START) {
        start = start->next;    // Make the second node, the 'start' node.
        free(ptr);
    }
    else {
        while (ptr) {

            // Find the second last node.
            if (ptr->next != NULL && (ptr->next)->next == NULL) {
                free(ptr->next); // Free last node.
                ptr->next = NULL;
                break;
            }

            ptr = ptr->next;
        }
    }

    printf("\n Node deleted! \n");
}

void showNodes() {
    struct list *ptr = start;

    printf("Start->");
    while (ptr) { // Loop the whole linked list.
        printf("%d->", ptr->data);
        ptr = ptr->next;
    }

    printf("NULL\n");
}
