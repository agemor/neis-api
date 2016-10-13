package org.hyunjun.school;

/**
 * School API
 * 전국 교육청 소속 교육기관의 학사일정, 메뉴를 간단히 불러올 수 있습니다.
 *
 * @author HyunJun Kim
 * @version 3.0.3
 */
public class SchoolException extends Exception {

    public SchoolException() {}

    public SchoolException(String message) {
        super(message);
    }
}
