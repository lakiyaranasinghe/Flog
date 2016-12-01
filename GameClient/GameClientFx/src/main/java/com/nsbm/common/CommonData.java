/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsbm.common;

/**
 *
 * @author Lakshitha
 */
public class CommonData {

    public static String username;
    public static String letters;
    public static String initialLetters;
    public static int currentRound = 0;
    public static boolean isWaiting = false;
    public static int specialPoints = 0;
    public static int id;
    public static boolean isLastPlayer = false;
    public final static String IP = "http://flogame-nsbm.rhcloud.com/WebResources/";
//    public final static String IP = "http://localhost:8080/FloGame/WebResources/";
    public static PlayerStatus playerStatus = PlayerStatus.JOINED;
    public final static String GET = "GET";
    public final static String POST = "POST";
    public final static String PLAYER_CLASS = "PlayerService/";
    public final static String GAME_CLASS = "GameService/";
    public final static String BROADCAST = "BroadCaster/";
    public final static String ADD_PLAYER = "addPlayer";
    public final static String GET_PLAYERS = "getPlayers";
    public final static String PLAYER_JOIN_BROADCAST = "sendPlayerJoin";
    public final static String NEXT_ROUND_BROADCAST = "sendNextRoundStart";
    public final static String PLAYER_JOIN_LISTEN = "listenPlayerJoin";
    public final static String ROUND_COMPLETION_LISTEN = "listenRoundCompletion";
    public final static String WORD_CLASS = "WordService/";
    public final static String INITIAL_LETTERS = "getInitialLetters";
    public final static String LETTERS = "getLetters";
    public final static String ROUND_COMPLETION_BROADCAST = "sendPlayerRoundCompletion";
    public final static String GET_ROUND_COMPLETED_PLAYERS = "getRoundCompletedPlayers";
    public final static String POINT_CLASS = "PointService/";
    public final static String GET_SPECIAL_POINTS = "getSpecialPoints";
    public final static String REMOVE_PLAYER = "removePlayer";
    public final static String START_GAME = "startGame";
    public final static String FINAL_SCORE = "getFinalScore";
    public final static String CHANGE_LETTER = "changeLetter";
    public final static String SUBMIT_WORD = "submitWord";
    public final static String TERMINATE_PLAYER = "terminatePlayer";
    public final static String CHECK_TERMINATION = "checkTermination";
    public final static String REGISTER_PLAYER = "registerPlayer";
}
