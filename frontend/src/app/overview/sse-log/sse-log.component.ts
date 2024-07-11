import { Component, Signal, inject } from '@angular/core';
import { SseService } from '../../shared/_services/sse.service';
import { SseLog } from '../../shared/_models/sse-log';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-sse-log',
  standalone: true,
  imports: [],
  templateUrl: './sse-log.component.html',
  styleUrl: './sse-log.component.css'
})
export class SseLogComponent {

}
