package jp.co.metateam.library.model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import jp.co.metateam.library.repository.RentalManageRepository;
import jp.co.metateam.library.model.RentalManage;
import lombok.AllArgsConstructor;
import jp.co.metateam.library.values.RentalStatus;

/**
 * 貸出管理DTO
 */
@Getter
@Setter
public class RentalManageDto {

    private Long id;

    @NotEmpty(message="在庫管理番号は必須です")
    private String stockId;

    @NotEmpty(message="社員番号は必須です")
    private String employeeId;

    @NotNull(message="貸出ステータスは必須です")
    private Integer status;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @NotNull(message="貸出予定日は必須です")
    private Date expectedRentalOn;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @NotNull(message="返却予定日は必須です")
    private Date expectedReturnOn;

    

    private Timestamp rentaledAt;

    private Timestamp returnedAt;

    private Timestamp canceledAt;

    private Stock stock;

    private Account account;

    public  Optional<String> updatestatus(Integer oldstatus){

    if (oldstatus == RentalStatus.RENT_WAIT.getValue() && this.status == RentalStatus.RETURNED.getValue()) {
        return Optional.of("貸出ステータスは「貸出待ち」→「返却済み」には変更できません。");
    }else if (oldstatus == RentalStatus.RENTAlING.getValue() && this.status == RentalStatus.RENT_WAIT.getValue()) {
        return Optional.of("貸出ステータスは「貸出中」→「貸出待ち」には変更できません。");
    }else if (oldstatus == RentalStatus.RENTAlING.getValue() && this.status == RentalStatus.CANCELED.getValue()) {
        return Optional.of("貸出ステータスは「貸出中」→「キャンセル」には変更できません。");
    }else if (oldstatus == RentalStatus.RETURNED.getValue() && this.status == RentalStatus.RENT_WAIT.getValue()) {
        return Optional.of("貸出ステータスは「返却済み」→「貸出待ち」には変更できません。");
    }else if (oldstatus == RentalStatus.RETURNED.getValue() && this.status == RentalStatus.RENTAlING.getValue()) {
        return Optional.of("貸出ステータスは「返却済み」→「貸出中」には変更できません。");
    }else if (oldstatus == RentalStatus.RETURNED.getValue() && this.status == RentalStatus.CANCELED.getValue()) {
        return Optional.of("貸出ステータスは「返却済み」→「キャンセル」には変更できません。");
    }else if (oldstatus == RentalStatus.CANCELED.getValue() && this.status == RentalStatus.RENT_WAIT.getValue()) {
        return Optional.of("貸出ステータスは「キャンセル」→「貸出待ち」には変更できません。");
    }else if (oldstatus == RentalStatus.CANCELED.getValue() && this.status == RentalStatus.RENTAlING.getValue()) {
        return Optional.of("貸出ステータスは「キャンセル」→「貸出中」には変更できません。");
    }else if (oldstatus == RentalStatus.CANCELED.getValue() && this.status == RentalStatus.RETURNED.getValue()) { 
        return Optional.of("貸出ステータスは「キャンセル」→「返却済み」には変更できません。");
    }
        return Optional.empty();

    }     


    public void dateCheck() throws Exception {
        LocalDate expectedRentalOnLocalDate = this.expectedRentalOn.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate expectedReturnOnLocalDate = this.expectedReturnOn.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
 
        if (expectedRentalOnLocalDate.isAfter(expectedReturnOnLocalDate)) {
            throw new Exception("返却予定日は貸出予定日よりも後に設定してください");
        }
    }

    public  Optional<String> checkScheduledDateForStatus(Integer oldstatus,Date expectedRentalOn){
        LocalDate currentDate = LocalDate.now();
        if(oldstatus == RentalStatus.RENT_WAIT.getValue() && this.status == RentalStatus.RENTAlING.getValue()){
            if(expectedRentalOn != null){
                LocalDate scheduledDate = expectedRentalOn.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (scheduledDate.isEqual(currentDate)){
            return Optional.of("貸出ステータスは貸出予定日のみ貸出中に変更できます");

        }
        
      }
    }
    return Optional.empty();
    }
}
